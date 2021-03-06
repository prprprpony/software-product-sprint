// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet that returns comments data.*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private final DatastoreService mDatastore = DatastoreServiceFactory.getDatastoreService();
  private final ArrayList<String> mComments = new ArrayList<String>();
  private final UserService mUserService = UserServiceFactory.getUserService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    final PreparedQuery results = mDatastore.prepare(query);
    mComments.clear();
    for (Entity entity : results.asIterable()) {
      mComments.add((String) entity.getProperty("text"));
    }
    response.setContentType("application/json;");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(new Gson().toJson(mComments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!mUserService.isUserLoggedIn()) {
      response.sendRedirect("/index.html?login");
      return;
    }
    final String text = getParameter(request, "text-input", "");
    final long timestamp = System.currentTimeMillis();
    final String userEmail = mUserService.getCurrentUser().getEmail();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", text);
    commentEntity.setProperty("timestamp", timestamp);
    commentEntity.setProperty("userEmail", userEmail);

    mDatastore.put(commentEntity);
    response.sendRedirect("/index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    final String value = request.getParameter(name);
    return value == null ? defaultValue : value;
  }
}
