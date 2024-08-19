package juuxel.adorn.block.variant;

public enum BlockKind {
    CHAIR("chair"),
    TABLE("table"),
    DRAWER("drawer"),
    KITCHEN_COUNTER("kitchen_counter"),
    KITCHEN_CUPBOARD("kitchen_cupboard"),
    KITCHEN_SINK("kitchen_sink"),
    POST("post"),
    PLATFORM("platform"),
    STEP("step"),
    SHELF("shelf"),
    COFFEE_TABLE("coffee_table"),
    BENCH("bench"),
    ;

    private final String id;

    BlockKind(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
