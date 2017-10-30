package com.apitest.common

class TreeNode<T>(val obj:T){

    val children:MutableList<T> = ArrayList()
        get() = ArrayList(field)

    var parent:TreeNode<T> = this

    fun add(node:T):TreeNode<T>{
        var treeNode = TreeNode(node)
        children.add(node)
        treeNode.parent = this
        return treeNode
    }
}





