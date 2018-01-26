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

    /**
     * 唯一标识；
     */
    @Getter
    @Setter
    private String id;

    /**
     * 文件名称；
     */
    @Getter
    @Setter
    private String fileName;

    /**
     * 文件路径；
     */
    @Getter
    @Setter
    private String filePath;

    /**
     * 文件内容；
     */
    @Getter
    @Setter
    private String fileBody;

    /**
     * 文件标签；
     */
    @Getter
    @Setter
    private String fileFlag;

    public FileWrapper(String fileName, String filePath, String fileBody) {
        // MD5
        this.id = StringUtils.md5(filePath);
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileBody = fileBody;

        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            try {
                String text = FileUtils.readFileToString(file);
                if (!StringUtils.isEmpty(text) && text.equals(fileBody)) {
                    // 文件已存在，且内容相同；
                    this.fileFlag = FLAG_OP_SKIP;
                } else {
                    // 文件已存在，内容有差异；
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
     * 保存文件。
     */
    public Map<String, String> renderFile() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("code", "0");

        // 检查是否跳过生成步骤；
        if (fileFlag.equals(FLAG_OP_SKIP)) {
            result.put("code", "1");
            result.put("info", fileName + "文件的内容没有变化，已跳过改文件，无需重复生成！");
            return result;
        }

        // 检查模板内容是否为空；
        if (StringUtils.isEmpty(fileBody)) {
            result.put("info", fileName + "的模板文件已经损坏，无法生成文件！");
            return result;
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write(fileBody);
            writer.flush();

            result.put("code", "1");
            result.put("info", filePath + "文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();

            // 拼接错误日志；
            String info = filePath + "文件生成失败！<br/><br/>";
            info += "错误日志：<br/>";
            info += StringUtils.wrapNull(e.getMessage()).replaceAll("(\r\n|\r|\n|\n\r)", "<br/>");
            result.put("info", info);
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
     * 预览文件。
     */
    public void renderText() {
        String text = fileBody.replaceAll("(\r\n|\r|\n|\n\r)", "<br/>");
    }

    /**
     * 比较文件。
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
