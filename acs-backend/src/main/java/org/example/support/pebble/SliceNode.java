package org.example.support.pebble;

import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.extension.NodeVisitor;
import io.pebbletemplates.pebble.node.AbstractRenderableNode;
import io.pebbletemplates.pebble.node.expression.Expression;
import io.pebbletemplates.pebble.node.expression.MapExpression;
import io.pebbletemplates.pebble.template.EvaluationContextImpl;
import io.pebbletemplates.pebble.template.PebbleTemplateImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SliceNode extends AbstractRenderableNode {

    private final Expression<?> pathExpression;
    private final MapExpression mapExpression;

    public SliceNode(int lineNumber, Expression<?> pathExpression, MapExpression mapExpression) {
        super(lineNumber);
        this.pathExpression = pathExpression;
        this.mapExpression = mapExpression;
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContextImpl context) throws IOException {

        String path = (String) this.pathExpression.evaluate(self, context);

        Map<?,?> map = Collections.emptyMap();
        if (this.mapExpression != null) {
            map = this.mapExpression.evaluate(self, context);
        }

        if (path == null) {
            throw new PebbleException(null,
                    "The path name in an slice tag evaluated to NULL. If the path name is static, make sure to wrap it in quotes.",
                    this.getLineNumber(), self.getName());
        }

        String params = map.entrySet()
                           .stream()
                           .map(entry -> entry.getKey().toString() + "=" + URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8))
                           .reduce((p1, p2) -> p1 + "&" + p2)
                           .orElse("");

        log.info("{}", params);

        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        String dispatchURI = path;
        if (! StringUtils.isEmpty(params)) {
            dispatchURI = dispatchURI + "?" + params;
        }
        log.info("{}", dispatchURI);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(dispatchURI);
        try {
            if (requestDispatcher == null) {
                throw new ServletException("requestDispatcher is null");
            }
            requestDispatcher.include(request, response);
        } catch (ServletException e) {
            throw new PebbleException(e,
                    "The path name in an slice tag evaluated to exception.",
                    this.getLineNumber(), self.getName());
        }

    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    public Expression<?> getPathExpression() {
        return pathExpression;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest();
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getResponse();
    }

}
