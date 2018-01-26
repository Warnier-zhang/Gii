package org.mybatis.gii.generator;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.mybatis.gii.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileWrapper {
    public static final String FLAG_OP_CREATE = "新建";
    public static final String FLAG_OP_OVERWRITE = "替换";
    public static final String FLAG_OP_SKIP = "跳过";

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String fileName;

    @Getter
    @Setter
    private String filePath;

    @Getter
    @Setter
    private String fileBody;

    @Getter
    @Setter
    private String fileFlag;

    public FileWrapper(String fileName, String filePath, String fileBody) {
        this.id = StringUtils.md5(filePath);
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileBody = fileBody;

        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            try {
                String fileText = FileUtils.readFileToString(file);
                if (fileText.equals(fileBody)) {
                    this.fileFlag = FLAG_OP_SKIP;
                } else {
                    this.fileFlag = FLAG_OP_OVERWRITE;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.fileFlag = FLAG_OP_CREATE;
        }
    }

    /**
     * 保存文件
     */
    public Map<String, String> renderFile() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("code", "0");
        result.put("info", "保存源码文件失败！");

        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write(fileBody);
            writer.flush();
            result.put("code", "1");
            result.put("info", "保存源码文件成功！");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 预览文件
     */
    public void renderText() {
        String text = fileBody.replaceAll("(\r\n|\r|\n|\n\r)", "<br/>");
    }

    /**
     * 比较文件
     */
    public void renderDiff() {
        List<String> newLines = Arrays.asList(fileBody.split("\n"));
        try {
            List<String> oldLines = FileUtils.readLines(new File(filePath));
            String diffs = StringUtils.diff(oldLines, newLines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
