package models

import javax.inject.Singleton

import scala.collection.mutable.ListBuffer

case class Task(label: String)

@Singleton
class TaskRepository {

  var tasks: ListBuffer[Task] = ListBuffer()

  def create(task : Task) : Unit = {
    tasks += task
  }

  def delete(id: Int) : Unit = tasks.remove(id)

  def find(id: Int): Task = tasks(id)

  def findAll(): List[Task] = tasks.toList

}
