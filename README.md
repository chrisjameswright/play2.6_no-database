# Scala + Play 2.6 example (without database)

This is a basic example to demonstrate how a very simple Play application is structured.

## conf/routes

  This tells Play how to take a HTTP request sent to a given URL and decide which function to call in our code. For example:
  ```
  GET     /    controllers.HomeController.index
  ```
  ...means that a `GET` request sent without specifying a page (i.e., `http://localhost:9000/`) will cause the `index` function of `HomeController` to be executed to produce a response.

  Similarly, the line:
  ```
  POST    /add    controller/HomeController.add
  ```
  ...means that a `POST` request sent to the `add` page (i.e., `http://localhost:9000/add`) will cause the `add` function of `HomeController` to be executed. Note that although these both have the value `add`, they don't need to be the same.

  Further information: 
    * [Documentation on routing](https://www.playframework.com/documentation/2.6.x/ScalaRouting)

## app/controllers/HomeController

  Controllers are responsible deciding what action to take in response. The definition of this class contains a bit of boilerplate code that might look a little intimidating:
  ```scala
@Singleton
class HomeController @Inject()(taskRepository: TaskRepository, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {
  ```
  
  Breaking that down, at a fairly high level:
    * `@Singleton` - think of this as making the class behave like a Scala object, such that we only get one instance.
    * `@Inject()(taskRepository: TaskRepository, cc: ControllerComponents)` - this is a constructor with something called _dependency injection_, which says this class needs an instance of `TaskRepository` (our class for storing tasks) and `ControllerComponents` (part of Play that contains various things we need), but without us needing to pass instances in ourselves.
    * `extends AbstractController(cc)` - an abstract class we can use as a base for our controllers, to give us access to various parts of Play.
    * `with play.api.i18n.I18nSupport` - a trait to provide access to some of Play's support for internationalisation ('i18n').
    
  From our routes, we can see that our "homepage" requests cause our `index` function to be called:
  ```scala
  def index() = Action { implicit request: Request[AnyContent] =>
    val data = taskRepository.findAll()
    Ok(views.html.index(data, todoForm))
  }
  ```
  
  In this case, `Action` is basically a function that takes a request and returns some result to the client. In this case, we call our `taskRepository` to get all of the tasks currently stored and then pass them to our view, `views.html.index`, calling it as with any other function. We also pass an object `todoForm` which is defined later in the class as:
  
  ```scala
  val todoForm = Form(
    mapping(
      "label" -> text.verifying(nonEmpty)
    )(Task.apply)(Task.unapply)
  )
  ```
  
  This makes use of some functionality from Play to describe how we'll take data from a HTML form on a page and turn it into an instance of a case class, `Task`. In this case we're saying there is one field of text and it cannot be left empty.
  
  Further information: 
    * [Documentation on actions, controllers and results](https://www.playframework.com/documentation/2.6.x/ScalaActions)
    * [Documentation on forms](https://www.playframework.com/documentation/2.6.x/ScalaForms)
    
