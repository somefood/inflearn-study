package collection.list;

public class BatchProcessorMain {

    public static void main(String[] args) {

        MyArrayList<Integer> arrayList = new MyArrayList<>();
        BatchProcessor batchProcessor2 = new BatchProcessor(arrayList);
        batchProcessor2.logic(50_000);

        MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        BatchProcessor batchProcessor = new BatchProcessor(linkedList);
        batchProcessor.logic(50_000);
    }
}
