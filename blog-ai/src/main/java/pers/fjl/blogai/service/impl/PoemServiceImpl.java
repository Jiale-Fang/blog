package pers.fjl.blogai.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.python.core.*;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pers.fjl.blogai.service.PoemService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class PoemServiceImpl implements PoemService {
    @Value("${ai.pythonPath}")
    private String pythonPath;

    @Value("${ai.pyPath}")
    private String pyPath;

    private String result;

    @Override
    public String randomPoem(String methodNo) {
        return getPoems("", methodNo);
    }

    @Override
    public String acrosticPoem(String words, String methodNo) {
        return getPoems(words, methodNo);
    }

    @Override
    public String randomPoem2(String words, String methodNo) {
        return getPoems(words, methodNo);
    }

    public String getPoems(String words, String methodNo) {
        String myString = null;
        String[] arguments = new String[]{pythonPath, pyPath, methodNo, words};
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = in.readLine()) != null) {
                result = line;
            }
            in.close();
            int re = process.waitFor();
            if (re == 1) { // 出错了
                while ((myString = stderrReader.readLine()) != null) {
                    log.error("PythonException:[{}]", myString);
                }
                throw new BizException("python脚本出错了");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
