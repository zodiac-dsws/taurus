package br.cefetrj.sagitarii.parser;

// Generated from d:\parser\src\main\antl4\Sagitarii.g by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SagitariiParser}.
 */
public interface SagitariiListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SagitariiParser#activitySentence}.
	 * @param ctx the parse tree
	 */
	void enterActivitySentence(SagitariiParser.ActivitySentenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SagitariiParser#activitySentence}.
	 * @param ctx the parse tree
	 */
	void exitActivitySentence(SagitariiParser.ActivitySentenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SagitariiParser#inputSentence}.
	 * @param ctx the parse tree
	 */
	void enterInputSentence(SagitariiParser.InputSentenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SagitariiParser#inputSentence}.
	 * @param ctx the parse tree
	 */
	void exitInputSentence(SagitariiParser.InputSentenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SagitariiParser#executor}.
	 * @param ctx the parse tree
	 */
	void enterExecutor(SagitariiParser.ExecutorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SagitariiParser#executor}.
	 * @param ctx the parse tree
	 */
	void exitExecutor(SagitariiParser.ExecutorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SagitariiParser#input_relation}.
	 * @param ctx the parse tree
	 */
	void enterInput_relation(SagitariiParser.Input_relationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SagitariiParser#input_relation}.
	 * @param ctx the parse tree
	 */
	void exitInput_relation(SagitariiParser.Input_relationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SagitariiParser#output_relation}.
	 * @param ctx the parse tree
	 */
	void enterOutput_relation(SagitariiParser.Output_relationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SagitariiParser#output_relation}.
	 * @param ctx the parse tree
	 */
	void exitOutput_relation(SagitariiParser.Output_relationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SagitariiParser#activity_name}.
	 * @param ctx the parse tree
	 */
	void enterActivity_name(SagitariiParser.Activity_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SagitariiParser#activity_name}.
	 * @param ctx the parse tree
	 */
	void exitActivity_name(SagitariiParser.Activity_nameContext ctx);
}