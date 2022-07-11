public enum Opcodes {
    PUSH,
    POP,
    ADD,
    SUB,
    MUL,
    DIV,
    MOD,
    COMPARE,
    IFEQ,       //if comparison A and B are equal
    IFNE,       //if comparison A and B are not equal
    IFGT,       //if comparison A and B where A greater than B
    IFLT,       //if comparison A and B where A less than B
    AND,
    OR,
    NOT,
    XOR,
    NOP,
    MARKER,
    HOP,
    SYSTEM,
    TRANSFER,
    ALLOC;

    public static boolean contains(String value){
        for (Opcodes op : Opcodes.values()) {
            if (op.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
