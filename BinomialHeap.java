/**
 * Represents a binomial heap data structure.
 * <p>
 * A binomial heap is a collection of binomial trees. Each binomial tree in the heap follows the binomial heap properties.
 * This implementation supports operations like insertion, deletion of the minimum element, finding the minimum element,
 * decreasing key, and deletion of a specific element.
 */
public class BinomialHeap {
    private int size;
    private HeapNode last;
    private HeapNode min;
    private int numOfTrees;

    /**
     * Constructs an empty binomial heap.
     * Complexity: O(1)
     */
    public BinomialHeap() {
        this.size = 0;
        this.last = null;
        this.min = null;
        this.numOfTrees = 0;
    }

    /**
     * Constructs a binomial heap with a single node containing the given HeapItem.
     * Complexity: O(1)
     *
     * @param newNode The node to be added to the heap.
     */
    public BinomialHeap(HeapNode newNode) {
        this.min = newNode;
        this.size = 1;
        this.last = newNode;
        this.last.setNext(this.last);
    }

    /**
     * Inserts a new element with the given key and info into the binomial heap.
     * Returns the HeapItem associated with the inserted element.
     * Complexity: O(log n)
     *
     * @param key  The key of the element to be inserted.
     * @param info Additional information associated with the element.
     * @return The HeapItem associated with the inserted element.
     */
    public HeapItem insert(int key, String info) {
        if (key <= 0) {
            throw new IllegalArgumentException("Key must be greater than 0.");
        }

        HeapNode newNode = new HeapNode();
        newNode.item = new HeapItem(key, info);
        newNode.item.node = newNode;
        newNode.rank = 0;
        newNode.parent = null;
        newNode.child = null;
        newNode.next = null;

        if (this.isEmpty()) {
            this.min = newNode;
            this.last = newNode;
            this.numOfTrees = 1;
            this.size = 1;
            newNode.next = newNode;
        } else {
            this.meld(new BinomialHeap(newNode));
        }

        return newNode.item;
    }

    /**
     * Deletes the minimum element from the binomial heap.
     * Complexity: O(log n)
     */
    public void deleteMin() {
        if (this.isEmpty()) {
            throw new IllegalStateException("Heap is empty. Cannot delete minimum element.");
        }

        if (this.size == 1) {
            this.size = 0;
            this.last = null;
            this.min = null;
            this.numOfTrees = 0;
            return;
        }
        if (this.min.child != null) {
            HeapNode child = this.min.child;
            HeapNode minChild = this.min.child;
            HeapNode first = this.min.child;
            int cntTrees = 0;
            int size = (int) Math.pow(2, this.min.rank) - 1;
            boolean flag = true;
            while (child != first || flag) {
                flag = false;
                if (minChild.item.key > child.item.key) {
                    minChild = child;
                }
                child.parent = null;
                child = child.next;
                cntTrees++;
            }
            BinomialHeap childHeap = new BinomialHeap();
            childHeap.min = minChild;
            childHeap.last = first;
            childHeap.numOfTrees = cntTrees;
            childHeap.size = size;

            if (this.numOfTrees > 1) {
                deleteMinHeapNode();
                this.meld(childHeap);
            } else {
                this.last = childHeap.last;
                this.size = childHeap.size;
                this.min = childHeap.min;
                this.numOfTrees = childHeap.numOfTrees;
            }
        } else {
            deleteMinHeapNode();
        }
    }

    private void deleteMinHeapNode() {
        if (this.min == null) {
            throw new IllegalStateException("Heap is empty. Cannot delete minimum element.");
        }

        int minSize = (int) Math.pow(2, this.min.rank) - 1;
        HeapNode curr = this.last;
        HeapNode newMin = this.last;
        for (int i = 0; i < this.numOfTrees; i++) {
            if (curr != this.min && curr.item.key < newMin.item.key) {
                newMin = curr;
            }
            if (curr.next == this.min) {
                curr.next = this.min.next;
            }
            curr = curr.next;
        }
        if (this.last == this.min) {
            this.last = this.min.next;
        }
        if (this.min.child != null) {
            this.size -= minSize + 1;
        } else {
            this.size--;
        }
        this.min = newMin;
        this.numOfTrees -= 1;
    }

    /**
     * Finds and returns the minimum element from the binomial heap.
     *
     * @return The HeapItem representing the minimum element.
     */
    public HeapItem findMin() {
        if (this.isEmpty())
            return null;
        return this.min.getItem();
    }

    /**
     * Decreases the key of the specified HeapItem by the given difference.
     * After decreasing the key, the heap property is restored.
     * Complexity: O(log n)
     *
     * @param item The HeapItem whose key needs to be decreased.
     * @param diff The difference by which the key needs to be decreased.
     */
    public void decreaseKey(HeapItem item, int diff) {
        if (item == null) {
            throw new IllegalArgumentException("HeapItem cannot be null.");
        }

        item.node.item.key -= diff;
        shiftUp(item);
    }

    protected void shiftUp(HeapItem item) {
        if (item == null || item.node == null) {
            throw new IllegalArgumentException("HeapItem cannot be null.");
        }

        HeapNode node = item.node;
        HeapNode parent = item.node.parent;
        while (node.parent != null && parent.item.key > node.item.key) {
            node.swapItem(parent);
            node = parent;
            parent = parent.parent;
        }
        if (node.item.key < this.min.item.key) {
            this.min = node;
        }
    }

    /**
     * Deletes the specified element from the binomial heap.
     * Complexity: O(log n)
     *
     * @param item The HeapItem representing the element to be deleted.
     */
    public void delete(HeapItem item) {
        if (item == null) {
            throw new IllegalArgumentException("HeapItem cannot be null.");
        }

        decreaseKey(item, Integer.MIN_VALUE);
        deleteMin();
    }

    /**
     * Merges the given binomial heap with the current binomial heap.
     * Complexity: O(log n)
     *
     * @param heap2 The binomial heap to be merged with the current heap.
     */
    public void meld(BinomialHeap heap2) {
        if (heap2 == null) {
            throw new IllegalArgumentException("Heap2 cannot be null.");
        }

        if (heap2.isEmpty()) {
            return;
        }

        if (this.isEmpty()) {
            this.min = heap2.min;
            this.last = heap2.last;
            this.numOfTrees = heap2.numOfTrees;
            this.size = heap2.size;
            return;
        }

        this.connectHeaps(heap2);
        this.consolidate();
    }

    private void connectHeaps(BinomialHeap heap2) {
        if (heap2 == null) {
            throw new IllegalArgumentException("Heap2 cannot be null.");
        }

        HeapNode temp = heap2.last.next;
        heap2.last.next = this.last.next;
        this.last.next = temp;
        this.numOfTrees += heap2.numOfTrees;
        this.size += heap2.size;
    }

    private void consolidate() {
        if (this.isEmpty()) return;
        HeapNode[] rankArray = new HeapNode[(int) Math.ceil(Math.log(this.size) / Math.log(2)) + 1];
        HeapNode current = this.last;
        int treesVisited = 0;
        this.numOfTrees = 0;

        do {
            treesVisited++;
            HeapNode next = current.next;
            while (rankArray[current.rank] != null) {
                HeapNode treeWithSameRank = rankArray[current.rank];
                if (current.item.key > treeWithSameRank.item.key) {
                    HeapNode temp = current;
                    current = treeWithSameRank;
                    treeWithSameRank = temp;
                }
                linkSameDegree(current, treeWithSameRank);
                rankArray[current.rank - 1] = null;
            }
            rankArray[current.rank] = current;
            current = next;
        } while (treesVisited <= this.numOfTrees || current != this.last);

        this.last = null;
        this.min = null;
        for (HeapNode node : rankArray) {
            if (node != null) {
                if (this.last == null) {
                    this.last = node;
                    node.setNext(node);
                    this.min = node;
                } else {
                    node.setNext(this.last.next);
                    this.last.next = node;
                    this.last = node;
                    if (node.item.key < this.min.item.key) {
                        this.min = node;
                    }
                }
                this.numOfTrees++;
            }
        }
    }

    private void linkSameDegree(HeapNode node1, HeapNode node2) {
        if (node1 == null || node2 == null) {
            throw new IllegalArgumentException("Node1 and Node2 cannot be null.");
        }

        if (node1.item.key > node2.item.key) {
            HeapNode temp = node1;
            node1 = node2;
            node2 = temp;
        }
        if (node1.child == null) {
            node2.setNext(node2);
        } else {
            node2.setNext(node1.child.getNext());
            node1.child.setNext(node2);
        }
        node1.child = node2;
        node2.parent = node1;
        node1.rank++;
    }

    /**
     * Returns the number of elements in the heap.
     * Complexity: O(1)
     *
     * @return The number of elements in the heap.
     */
    public int size() {
        return this.size;
    }

    /**
     * Returns true if the heap is empty, otherwise false.
     * Complexity: O(1)
     *
     * @return True if the heap is empty, otherwise false.
     */
    public boolean isEmpty() {
        return last == null;
    }

    /**
     * Returns the number of trees in the heap.
     * Complexity: O(1)
     *
     * @return The number of trees in the heap.
     */
    public int numTrees() {
        return this.numOfTrees;
    }

    /**
     * Represents a node in a Binomial Heap.
     */
    public class HeapNode {
        private HeapItem item;
        private HeapNode child;
        private HeapNode next;
        private HeapNode parent;
        private int rank;

        /**
         * Returns the HeapItem associated with this node.
         * Complexity: O(1)
         *
         * @return The HeapItem associated with this node.
         */
        public HeapItem getItem() {
            return item;
        }

        /**
         * Swaps the HeapItem of this node with another HeapNode.
         * Complexity: O(1)
         *
         * @param other The other HeapNode to swap with.
         */
        protected void swapItem(HeapNode other) {
            HeapItem temp = this.item;
            this.item = other.item;
            other.item = temp;
            this.item.node = this;
            other.item.node = other;
        }

        /**
         * Returns the next node in the heap.
         * Complexity: O(1)
         *
         * @return The next node in the heap.
         */
        public HeapNode getNext() {
            return next;
        }

        /**
         * Sets the next node in the heap.
         * Complexity: O(1)
         *
         * @param next The next node to be set.
         */
        public void setNext(HeapNode next) {
            this.next = next;
        }
    }

    /**
     * Represents an item in a Binomial Heap.
     */
    public class HeapItem {
        private HeapNode node;
        private int key;
        private String info;

        /**
         * Constructs a HeapItem with the given key and info.
         * Complexity: O(1)
         *
         * @param key  The key of the HeapItem.
         * @param info Additional information associated with the HeapItem.
         */
        public HeapItem(int key, String info) {
            this.node = null;
            this.key = key;
            this.info = info;
        }
    }
}
