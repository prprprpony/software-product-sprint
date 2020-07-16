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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ["Pop Pop!",
      "Leonard likes this post.",
      "I can't believe I spent ten bucks on this! I don't know any of these people!",
      "My name is Alex!",
      "We're the only species on earth that observes shark week.",
      "TV's the best dad there is. TV never came home drunk, TV never forgot me at the zoo, TV never abused and insulted me... unless you count Cop Rock.",
      "The funny thing about being smart, is that you can get through most of life without ever having to do any work.",
      "I see your value now.",
      "Our first assignment is a documentary. The're like real movies, but with ugly people.",
      "I looked inside Nicolas Cage and I found a secret: People are random and pointless.",
      "I painted a tunnel on the side of the library. When it dries, I'm going for it.",
      "Do you understand what a conspiracy is? When you conspire with everyone you come across, you're not really conspiring with anyone. You're just doing random crap.",
      "It's not a pen, it's a principle!",
    ];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function getData() {
  fetch('/data').then(response => response.json()).then(arr => {
    const dataContainer = document.getElementById('data-container');
    arr.forEach(str => {
      var comment = document.createElement('p');
      comment.innerText = str;
      dataContainer.appendChild(document.createElement('hr'));
      dataContainer.appendChild(comment);
    });
  });
  fetch('/user').then(response => response.json()).then(userStatus => {
    const userContainer = document.getElementById('user-container');
    if (userStatus.isUserLoggedIn) {
      userContainer.innerHTML = '<span style="float:left;">username: ' + userStatus.userEmail + '</span>'
                                +'<span style="float:right;"><a href="' + userStatus.logoutUrl + '">logout</a></span>';
    } else {
      userContainer.innerHTML = '<span style="float:left;">username: None</span>'
                                +'<span style="float:right;"><a href="' + userStatus.loginUrl + '">login</a></span>';
    }
  });
  if (window.location.search == '?login') {
    alert("Pleas login to leave comments.");
  }
}