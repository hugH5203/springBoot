package com.wenliuniversity.springbootTest.exception;

/**自定义异常继承运行期异常
 * @author HuangHai
 * @date 2021/4/13 20:02
 */
public class MyException extends RuntimeException {
    public MyException() {
        super("出错了。。。。。。");
    }
}
