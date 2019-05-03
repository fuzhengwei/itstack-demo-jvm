package org.itstack.demo.jvm.instructions.references;

import org.itstack.demo.jvm.instructions.base.InstructionNoOperands;
import org.itstack.demo.jvm.rtda.Frame;
import org.itstack.demo.jvm.rtda.OperandStack;
import org.itstack.demo.jvm.rtda.Thread;
import org.itstack.demo.jvm.rtda.heap.methodarea.Object;

/**
 * http://www.itstack.org
 * create by fuzhengwei on 2019/5/2
 */
public class ATHROW extends InstructionNoOperands {

    @Override
    public void execute(Frame frame) {
        Object ex = frame.operandStack().popRef();
        if (ex == null) {
            throw new NullPointerException();
        }
    }

    public boolean findAndGotoExceptionHandler(Thread thread, Object ex) {
        do {
            Frame frame = thread.currentFrame();
            int pc = frame.nextPC() - 1;

            int handlerPc = frame.method().findExceptionHandler(ex.clazz(), pc);
            if (handlerPc > 0) {
                OperandStack stack = frame.operandStack();
                stack.clear();
                stack.pushRef(ex);
                frame.setNextPC(handlerPc);
                return true;
            }

            thread.popFrame();
        } while (!thread.isStackEmpty());
        return false;
    }
}
