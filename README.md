Binomial Heap Implementation Project
Overview
This project is a practical implementation of a non-lazy Binomial Heap data structure. The Binomial Heap is implemented in Java as part of a course on Data Structures at Tel Aviv University. The project focuses on implementing core operations of the Binomial Heap and analyzing their performance.

Features
The project involves implementing the following key operations for the Binomial Heap:

Insert(k, info): Insert an element with key k and string info.
DeleteMin(): Delete the element with the minimum key from the heap.
FindMin(): Return the element with the minimum key in the heap.
DecreaseKey(x, j): Decrease the key of an element x by a value of j.
Delete(x): Delete the element x from the heap.
Meld(heap2): Merge the current heap with another heap heap2.
Size(): Return the number of elements in the heap.
Empty(): Check if the heap is empty.
NumTrees(): Return the number of binomial trees in the heap.
Additionally, two classes, HeapNode and HeapItem, are used to represent nodes and elements within the heap. The HeapNode class includes fields for the node's item, children, parent, and rank, while HeapItem stores the key and additional information associated with each element.

Performance Analysis
The time complexity of the implemented operations is analyzed and documented in the code. The implementation aims to achieve optimal time complexity as discussed in class
