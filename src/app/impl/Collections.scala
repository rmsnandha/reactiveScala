package app.impl

import app.NumberInterface
import org.junit.Test

import scala.collection.immutable.HashMap


/**
  * Some examples about how to use collections in scala
  */
class Collections extends Generic with NumberInterface {


  /**
    * How to create and iterate a Map collection
    */
  @Test def iterateMap(): Unit = {
    println(defaultImpl)
    val map = HashMap[String, Int]("1" -> 1, "2" -> 2, "3" -> 3)

    val sumMap = map.toStream
      .map(entry => {
        println(map.get(entry._1).get)
        entry._2
      })
      .filter(n => isHigherThan1(n))
      .sum
    println(sumMap)
  }

  /**
    * How to revert a map from key/value to value/key using scan
    */
  @Test def revertMap(): Unit = {
    val map = HashMap[Int, Int](1 -> 2, 3 -> 4, 5 -> 6)
    val revertedMap = map.toStream
      .map(entry => Map[Int, Int](entry._2 -> entry._1))
      .scan(HashMap())((map1, map2) => map1 ++ map2).last
    println(revertedMap)
  }


  /**
    * We a map with int/list pair and we revert as key for every item in the collection,
    * and we set the value as the current key
    */
  @Test def revertMapList(): Unit = {
    val map = HashMap[Int, List[Int]](1 -> List(3, 4), 4 -> List(5, 6))
    val revertedMap = map.toStream
      .flatMap(map => map._2.toStream
        .map(entry => Map[Int, Int](entry -> map._1))
        .scan(HashMap())((map1, map2) => map1 ++ map2))
      .scan(HashMap())((m, m1) => m ++ m1).last
    println(revertedMap)
  }

  /**
    * Iterate over a collection emit the items create a new string items and return a new collection
    */
  @Test def intToStringList(): Unit = {
    val list = List(1, 2, 3).toStream
      .map(entry => String.valueOf(entry))
      .toList
    print(list)
  }

  /**
    * Iterate over a collection, emit the items, create a new one through the previous values,
    * and return a new collection with this new values
    */
  @Test def intToNewIntList(): Unit = {
    val list = List(1, 2, 3).toStream
      .flatMap(entry => List(entry * 100))
      .toList
    print(list)
  }

  /**
    * How to create merge to list
    */
  @Test def appendList(): Unit = {
    val list = List(1, 2, 3)
    val list2 = List(4, 5, 6)
    val totalList = mergeList(list, list2)
    print(totalList)
  }


  /**
    * Scan operator allow us in an iterator get the previous emitted item and the new one and do an operation.
    * We need to provide an initial value for the scan, and as second argument a bi function where we priovide
    * the previous item emitted and the new one
    */
  @Test def scanIntegers(): Unit = {
    print(List(1, 2, 3).scan(0)(sumIntegerFunction)
    )
  }

  def sumIntegerFunction: (Int, Int) => Int = {
    (previousItem, newItem) => previousItem + newItem
  }

  /**
    * In this case we define as initial value an empty list, then in the function mergeList we merge the previous list
    * and the new one. Finally we just return the last item emitted.
    */
  @Test def scanList(): Unit = {
    val newList = List(1, 2, 3)
      .map(n => List(n * 100))
      .scan(List())(mergeList).last
    println(newList)
  }

  /**
    * This exmaple compare two lists and return a new list with the only the values that exist in both collections
    */
  @Test def listEqualThanList(): Unit = {
    val listA = List(1, 3, 5)
    val listEqualThanA = List(1, 4, 6, 5, 3).toStream
      .map(n => listA.toStream
        .filter(n1 => n1 == n)
        .toList)
      .scan(List())((l, l1) => l ++ l1)
      .last
    println(listEqualThanA)
  }

  def mergeList(prevResult: List[Int], currentItem: List[Int]): List[Int] = {
    prevResult ++ currentItem
  }


  override def isHigherThan1(num: Int): Boolean = {
    num > 1
  }


}



