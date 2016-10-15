package com.wugy.java.enumeration;

/**
 * Created by wugy on 2016/10/3.
 */
public enum EnumTest1 {

    PLUS("+") {
        @Override
        public int bind(int args1, int args2) {
            return args1 + args2;
        }
    },
    SUB("-") {
        @Override
        public int bind(int args1, int args2) {
            return args1 - args2;
        }
    };

    final String operation;

    EnumTest1(String operation) {
        this.operation = operation;
    }

    abstract int bind(int args1, int args2);
}
