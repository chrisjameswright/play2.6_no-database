package controllers

import javax.inject._

import models.{Task, TaskRepository}
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

@Singleton
class HomeController @Inject()(taskRepository: TaskRepository, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index() = Action { implicit request: Request[AnyContent] =>
    val data = taskRepository.findAll()
    Ok(views.html.index(data, todoForm))
  }

  def add() = Action { implicit request: Request[AnyContent] =>
    todoForm.bindFromRequest().fold(
      errors => BadRequest(views.html.index(taskRepository.findAll(), errors)),
      task => {
        taskRepository.create(task)
        Redirect(routes.HomeController.index())
      }
    )
  }

  val todoForm = Form(
    mapping(
      "label" -> text.verifying(nonEmpty)
    )(Task.apply)(Task.unapply)
  )
}
