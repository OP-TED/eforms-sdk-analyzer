package eu.europa.ted.eforms.sdk.analysis.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import eu.europa.ted.efx.xpath.XPath20BaseListener;
import eu.europa.ted.efx.xpath.XPath20Lexer;
import eu.europa.ted.efx.xpath.XPath20Parser;
import eu.europa.ted.efx.xpath.XPath20Parser.PredicateContext;
import eu.europa.ted.efx.xpath.XPath20Parser.StepexprContext;;

public class XPathSplitter extends XPath20BaseListener {

  private final CharStream inputStream;
  private final LinkedList<StepInfo> steps = new LinkedList<>();

  public XPathSplitter(CharStream inputStream) {
    this.inputStream = inputStream;
  }

  /**
   * Parses the given xpath and returns a list containing a {@link StepInfo} for
   * each step that the XPath is comprised of.
   */
  public static List<StepInfo> getSteps(String xpath) {

    final CharStream inputStream = CharStreams.fromString(xpath);
    final XPath20Lexer lexer = new XPath20Lexer(inputStream);
    final CommonTokenStream tokens = new CommonTokenStream(lexer);
    final XPath20Parser parser = new XPath20Parser(tokens);
    final ParseTree tree = parser.xpath();

    final ParseTreeWalker walker = new ParseTreeWalker();
    final XPathSplitter xpathParser = new XPathSplitter(inputStream);
    walker.walk(xpathParser, tree);

    return xpathParser.steps;
  }

 /**
   * Parses the given xpath and returns a list containing the element name for
   * each step that the XPath is comprised of.
   * A step corresponding to an attribute is ignored, and predicates are removed from each element.
   * So for "a/b/ns:foo[x = y]/@attr" this will return ("a", "b", "ns:foo")
   */
  public static List<String> getStepElementNames(String xpath) {
    return getSteps(xpath).stream()
        .filter(s -> !s.isAttribute())
        .map(StepInfo::getElementName)
        .collect(Collectors.toList());
  }

  /**
   * Helper method that returns the input text that matched a parser rule context. It is useful
   * because {@link ParserRuleContext#getText()} omits whitespace and other lexer tokens in the
   * HIDDEN channel.
   *
   * @param context
   * @return
   */
  private String getInputText(ParserRuleContext context) {
    return this.inputStream
        .getText(new Interval(context.start.getStartIndex(), context.stop.getStopIndex()));
  }

  int predicateMode = 0;

  private Boolean inPredicateMode() {
    return predicateMode > 0;
  }

  @Override
  public void exitStepexpr(StepexprContext ctx) {
    if (!inPredicateMode()) {
      this.steps.offer(new StepInfo(ctx, this::getInputText));
    }
  }

  @Override
  public void enterPredicate(PredicateContext ctx) {
    this.predicateMode++;
  }

  @Override
  public void exitPredicate(PredicateContext ctx) {
    this.predicateMode--;
  }

  public class StepInfo {
    String stepText;
    List<String> predicates;
    int a;
    int b;

    public StepInfo(StepexprContext ctx, Function<ParserRuleContext, String> getInputText) {
      this.stepText = getInputText.apply(ctx.step());
      this.predicates =
          ctx.predicatelist().predicate().stream().map(getInputText).collect(Collectors.toList());
    }
     
    protected StepInfo(String stepText, List<String> predicates, Interval interval) {
      this.stepText = stepText;
      this.predicates = predicates;
      this.a = interval.a;
      this.b = interval.b;
    }

    public String getElementName() {
      return stepText;
    }

    public Boolean isAttribute() {
      return this.stepText.startsWith("@");
    }

    public Boolean isVariableStep() {
      return this.stepText.startsWith("$");
    }

    public String getPredicateText() {
      return String.join("", this.predicates);
    }

    public Boolean isTheSameAs(final StepInfo contextStep) {

      // First check the step texts are the different.
      if (!Objects.equals(contextStep.stepText, this.stepText)) {
        return false;
      }

      // If one of the two steps has more predicates that the other,
      if (this.predicates.size() != contextStep.predicates.size()) {
        // then the steps are the same is the path has no predicates
        // or all the predicates of the path are also found in the context.
        return this.predicates.isEmpty() || contextStep.predicates.containsAll(this.predicates);
      }

      // If there are no predicates then the steps are the same.
      if (this.predicates.isEmpty()) {
        return true;
      }

      // If there is only one predicate in each step, then we can do a quick comparison.
      if (this.predicates.size() == 1) {
        return Objects.equals(contextStep.predicates.get(0), this.predicates.get(0));
      }

      // Both steps contain multiple predicates.
      // We need to compare them one by one.
      // First we make a copy so that we can sort them without affecting the original lists.
      List<String> pathPredicates = new ArrayList<>(this.predicates);
      List<String> contextPredicates = new ArrayList<>(contextStep.predicates);
      Collections.sort(pathPredicates);
      Collections.sort(contextPredicates);
      return pathPredicates.equals(contextPredicates);
    }

    public Boolean isPartOf(Interval interval) {
      return this.a >= interval.a && this.b <= interval.b;
    }
  }
}
