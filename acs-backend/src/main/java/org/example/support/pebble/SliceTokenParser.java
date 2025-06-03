package org.example.support.pebble;

import io.pebbletemplates.pebble.error.ParserException;
import io.pebbletemplates.pebble.lexer.Token;
import io.pebbletemplates.pebble.lexer.TokenStream;
import io.pebbletemplates.pebble.node.RenderableNode;
import io.pebbletemplates.pebble.node.expression.Expression;
import io.pebbletemplates.pebble.node.expression.MapExpression;
import io.pebbletemplates.pebble.parser.Parser;
import io.pebbletemplates.pebble.tokenParser.TokenParser;
import lombok.extern.slf4j.Slf4j;

/**
 * 新增一個 Slice Tag，使用
 * RequestDispatcher include 功能，動態載入，controller 的內容
 *
 *
 */
@Slf4j
public class SliceTokenParser implements TokenParser {

    @Override
    public String getTag() {
        return "slice";
    }

    @Override
    public RenderableNode parse(Token token, Parser parser) {
        TokenStream stream = parser.getStream();
        int lineNumber = token.getLineNumber();

        // skip the 'slice' token
        stream.next();
        // path
        Expression<?> pathExpression = parser.getExpressionParser().parseExpression();

        Token current = stream.current();
        MapExpression mapExpression = null;

        // We check if there is an optional 'with' parameter on the slice tag.
        if (current.getType().equals(Token.Type.NAME) && current.getValue().equals("with")) {

            // Skip over 'params'
            stream.next();

            Expression<?> parsedExpression = parser.getExpressionParser().parseExpression();

            if (parsedExpression instanceof MapExpression) {
                mapExpression = (MapExpression) parsedExpression;
            } else {
                throw new ParserException(null,
                        String.format("Unexpected expression '%1s'.", parsedExpression
                                .getClass().getCanonicalName()), token.getLineNumber(), stream.getFilename());
            }

        }

        stream.expect(Token.Type.EXECUTE_END);

        return new SliceNode(lineNumber, pathExpression, mapExpression);
    }

}
