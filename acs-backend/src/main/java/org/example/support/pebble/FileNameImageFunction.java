package org.example.support.pebble;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FileNameImageFunction implements Function {

    public static final String FUNCTION_NAME = "fileNameImage";

    protected static final String PARAM_FILENAME = "filename";

    protected List<String> argumentNames;
    private String contextPath;

    public FileNameImageFunction() {
        super();
        this.argumentNames = new ArrayList<>();
        this.argumentNames.add(PARAM_FILENAME);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        StringBuffer result = new StringBuffer();

        result.append(this.getContextPath());
        this.addFileNameParameter(args, result);

        return result.toString();
    }

    public String setImageURL(String fileName) {
    	
    	String imageURL = "/static/images/file/none.png";
        if (StringUtils.hasText(fileName)) {
            String ext = FilenameUtils.getExtension(fileName);

            switch (ext) {
                case "csv":
                    imageURL = "/static/images/file/csv.png";
                    break;
                case "xls":
                    imageURL = "/static/images/file/excel.png";
                    break;
                case "xlsx":
                    imageURL = "/static/images/file/excel.png";
                    break;
                case "bmp":
                    imageURL = "/static/images/file/image.png";
                    break;
                case "gif":
                    imageURL = "/static/images/file/image.png";
                    break;
                case "ico":
                    imageURL = "/static/images/file/image.png";
                    break;
                case "jpeg":
                    imageURL = "/static/images/file/image.png";
                    break;
                case "jpg":
                    imageURL = "/static/images/file/image.png";
                    break;
                case "png":
                    imageURL = "/static/images/file/image.png";
                    break;
                case "svg":
                    imageURL = "/static/images/file/image.png";
                    break;
                case "tiff":
                    imageURL = "/static/images/file/image.png";
                    break;
                case "mp3":
                    imageURL = "/static/images/file/mp3.png";
                    break;
                case "odg":
                    imageURL = "/static/images/file/odg.png";
                    break;
                case "odp":
                    imageURL = "/static/images/file/odp.png";
                    break;
                case "ods":
                    imageURL = "/static/images/file/ods.png";
                    break;
                case "odt":
                    imageURL = "/static/images/file/odt.png";
                    break;
                case "pdf":
                    imageURL = "/static/images/file/pdf.png";
                    break;
                case "ppt":
                    imageURL = "/static/images/file/powerpoint.png";
                    break;
                case "pptx":
                    imageURL = "/static/images/file/powerpoint.png";
                    break;
                case "pps":
                    imageURL = "/static/images/file/powerpoint.png";
                    break;
                case "rar":
                    imageURL = "/static/images/file/rar.png";
                    break;
                case "txt":
                    imageURL = "/static/images/file/text.png";
                    break;
                case "doc":
                    imageURL = "/static/images/file/word.png";
                    break;
                case "docx":
                    imageURL = "/static/images/file/word.png";
                    break;
                case "zip":
                    imageURL = "/static/images/file/zip.png";
                    break;
                default:
                    imageURL = "/static/images/file/none.png";
                    break;
            }
            
        }	
            
        return imageURL;
    	
    }

    private void addFileNameParameter(Map<String, Object> args, StringBuffer result) {
    	String fileName = (String) args.get(PARAM_FILENAME);
        result.append(setImageURL(fileName));
    }

    private String getContextPath() {
        if (this.contextPath == null) {
            this.contextPath = this.getRequest().getContextPath();
        }
        return this.contextPath;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public List<String> getArgumentNames() {
        return this.argumentNames;
    }

}
