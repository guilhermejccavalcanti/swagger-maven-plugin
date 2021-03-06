/**
 *  Copyright 2014 Reverb Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.springmvc;

import com.wordnik.sample.JavaRestResourceUtil;
import com.wordnik.sample.data.StoreData;
import com.wordnik.sample.exception.NotFoundException;
import com.wordnik.sample.model.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/store", produces = {"application/json", "application/xml"})
@Api(value="/store" , description = "Operations about store")
public class PetStoreResource {
  static StoreData storeData = new StoreData();
  static JavaRestResourceUtil ru = new JavaRestResourceUtil();


  @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
  @ApiOperation(value = "Find purchase order by ID",
    notes = "For valid response try integer IDs with value <= 5 or > 10. Other values will generated exceptions",
    response = Order.class)
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
      @ApiResponse(code = 404, message = "Order not found") })
  public ResponseEntity<Order> getOrderById(
      @ApiParam(value = "ID of pet that needs to be fetched", allowableValues = "range[1,5]", required = true) @PathVariable("orderId") String orderId)
      throws com.wordnik.sample.exception.NotFoundException {
    Order order = storeData.findOrderById(ru.getLong(0, 10000, 0, orderId));
    if (null != order) {
      return new ResponseEntity<Order>(order, HttpStatus.OK);
    } else {
      throw new NotFoundException(404, "Order not found");
    }
  }

  @RequestMapping(value = "/order", method = RequestMethod.POST)
  @ApiOperation(value = "Place an order for a pet")
  @ApiResponses({ @ApiResponse(code = 400, message = "Invalid Order") })
  public Order placeOrder(
      @ApiParam(value = "order placed for purchasing the pet",
        required = true) Order order) {
    storeData.placeOrder(order);
    return storeData.placeOrder(order);
  }

  @RequestMapping(value = "/order/{orderId}", method = RequestMethod.DELETE)
  @ApiOperation(value = "Delete purchase order by ID",
    notes = "For valid response try integer IDs with value < 1000. Anything above 1000 or nonintegers will generate API errors")
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
      @ApiResponse(code = 404, message = "Order not found") })
  public ResponseEntity deleteOrder(
      @ApiParam(value = "ID of the order that needs to be deleted", allowableValues = "range[1,infinity]", required = true) @PathVariable("orderId") String orderId) {
    storeData.deleteOrder(ru.getLong(0, 10000, 0, orderId));
    return new ResponseEntity(HttpStatus.OK);
  }

  @ApiOperation(value = "ping")
  @RequestMapping(value = "/ping", method = RequestMethod.GET)
  public ResponseEntity<String> ping() {
    return new ResponseEntity<String>("pong", HttpStatus.OK);
  }
}
