package app.event.list

import app.action.AppAction.Script
import app.infrastructure.{FrameworkRequest, FrameworkResponse}
import app.model.Codecs._
import app.model.EntityId
import argonaut.integrate.unfiltered.JsonResponse
import unfiltered.response.Ok

import scalaz.Scalaz._

object ListEventsController {

  def list(request: FrameworkRequest): Script[FrameworkResponse] =
    getEvents(request, None)

  def listForEntity(request: FrameworkRequest, entityId: EntityId): Script[FrameworkResponse] =
    getEvents(request, entityId.some)

  def getEvents(request: FrameworkRequest, entityId: Option[EntityId]): Script[FrameworkResponse] = {
    val pageSize = getFromRequest(request, "pageSize").flatMap(_.parseInt.toOption).getOrElse(10)
    val pageNumber = getFromRequest(request, "pageNumber").flatMap(_.parseLong.toOption)
    ListEventsService.getEvents(entityId, pageSize, pageNumber) map (events => Ok ~> JsonResponse(events))
  }

  def getFromRequest(request: FrameworkRequest, param: String): Option[String] =
    request.parameterValues(param).headOption
}
