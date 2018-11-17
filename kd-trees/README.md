# Programming Assignment 5: Kd-Trees

> Please navigate to the official [assignment 5 URL](http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html) for details.

Write a data type to represent a set of points in the unit square (all points have x- and y-coordinates between 0 and 1) using a 2d-tree to support efficient range search (find all of the points contained in a query rectangle) and nearest-neighbor search (find a closest point to a query point). 2d-trees have numerous applications, ranging from classifying astronomical objects to computer animation to speeding up neural networks to mining data to image retrieval.

**2d-tree implementation.** Write a mutable data type KdTree.java that uses a 2d-tree to implement the same API (but replace PointSET with KdTree). A 2d-tree is a generalization of a BST to two-dimensional keys. The idea is to build a BST with points in the nodes, using the x- and y-coordinates of the points as keys in strictly alternating sequence.

**Clients.**
The following interactive client programs help test the code.

* KdTreeVisualizer.java - computes and draws the 2d-tree that results from the sequence of points clicked by the user in the standard drawing window.
* RangeSearchVisualizer.java - reads a sequence of points from a file (specified as a command-line argument) and inserts those points into a 2d-tree. Then, it performs range searches on the axis-aligned rectangles dragged by the user in the standard drawing window.
* NearestNeighborVisualizer.java - reads a sequence of points from a file (specified as a command-line argument) and inserts those points into a 2d-tree. Then, it performs nearest-neighbor queries on the point corresponding to the location of the mouse in the standard drawing window.