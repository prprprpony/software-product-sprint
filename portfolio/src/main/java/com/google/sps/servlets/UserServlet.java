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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** Servlet that returns user info.*/
@WebServlet("/user")
public class UserServlet extends HttpServlet {
  private final static class UserStatus { 
    private boolean isUserLoggedIn;
    private String userEmail;
    private String logoutUrl;
    private String loginUrl;
    public UserStatus(boolean isUserLoggedIn, String userEmail, String logoutUrl, String loginUrl) {
        this.isUserLoggedIn = isUserLoggedIn;
        this.userEmail = userEmail;
        this.logoutUrl = logoutUrl;
        this.loginUrl = loginUrl;
    }
  }

  private final UserService mUserService = UserServiceFactory.getUserService();
  private UserStatus mUserStatus;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;");
    if (mUserService.isUserLoggedIn()) {
      mUserStatus = new UserStatus(true, mUserService.getCurrentUser().getEmail(), mUserService.createLogoutURL("/"), null);
    } else {
      mUserStatus = new UserStatus(false, null, null, mUserService.createLoginURL("/"));
    }
    response.setContentType("application/json;");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(new Gson().toJson(mUserStatus));
  }
}
