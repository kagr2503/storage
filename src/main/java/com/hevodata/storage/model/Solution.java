package com.hevodata.storage.model;


import java.util.*;

class Solution {
    public static boolean solution(int[] A, int[] B) {
        int N = A.length;

        // Create an array to represent disjoint sets
        int[] parent = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            parent[i] = i;
        }

        for (int i = 0; i < N; i++) {
            int rootA = findRoot(parent, A[i]);
            int rootB = findRoot(parent, B[i]);

            // If the two vertices have the same root, there is a cycle
            if (rootA == rootB) {
                return false;
            }

            // Union the two vertices
            parent[rootA] = rootB;
        }

        // Check if there is a single connected component
        int componentCount = 0;
        for (int i = 1; i <= N; i++) {
            if (parent[i] == i) {
                componentCount++;
            }
            if (componentCount > 1) {
                return false; // Multiple connected components
            }
        }

        // Check if each vertex has one incoming and one outgoing edge
        int[] inDegree = new int[N + 1];
        int[] outDegree = new int[N + 1];

        for (int i = 0; i < N; i++) {
            outDegree[A[i]]++;
            inDegree[B[i]]++;
        }

        for (int i = 1; i <= N; i++) {
            if (inDegree[i] != 1 || outDegree[i] != 1) {
                return false; // In-degree and out-degree are not equal to 1
            }
        }

        return true;
    }

    private static int findRoot(int[] parent, int node) {
        if (parent[node] == node) {
            return node;
        }
        return parent[node] = findRoot(parent, parent[node]);
    }

    public static  void main(String [] args){

        int[] A = {1,2,1};
        int[] B = {2,3,3};

        System.out.println(solution(A, B));

        int[] A1  = {3,1,2};
        int[] B1 = {2,3,1};
        System.out.println(solution(A1, B1));

        int[] A2  = {1,2,2,3,3};
        int[] B2 = {2,3,3,4,5};
        System.out.println(solution(A2, B2));

        int[] A3  = {1,2,3,4};
        int[] B3 = {2,1,4,4};
        System.out.println(solution(A3, B3));
    }


}
