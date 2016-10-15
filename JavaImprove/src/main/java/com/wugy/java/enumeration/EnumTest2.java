package com.wugy.java.enumeration;

/**
 * Created by wugy on 2016/10/3.
 */
public enum EnumTest2 implements Operation {

    PLUS("+") {
        @Override
        public int operate(int args1, int args2) {
            return args1 + args2;
        }
    },
    SUB("-") {
        @Override
        public int operate(int args1, int args2) {
            return args1 - args2;
        }
    };

    final String operation;

    EnumTest2(String operation) {
        this.operation = operation;
    }

}
