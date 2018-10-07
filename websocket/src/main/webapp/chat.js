function getContextPath(){
    var path = window.location.pathname;
    if(path.substr(path.length - 5) != ".html"){
        if(path.substr(path.length - 1) != "/"){
            path += "/";
        }
        path += "index.html";
    }
    console.log("path: " + path);
    var webCtx = path.substring(0, path.lastIndexOf('/'));
    return webCtx;
}

var host = window.location.host;
var webCtx = getContextPath();
console.log("context-path: " + webCtx);
var endPointURL = "ws://" + host + webCtx + "/chat";
console.log(endPointURL);
var chatClient = null;

function connect () {
    chatClient = new WebSocket(endPointURL);
    chatClient.onmessage = function (event) {
        var messagesArea = document.getElementById("messages");
        //alert(event.data);
        var message;
        try{
            var jsonObj = JSON.parse(event.data);
            message = jsonObj.user + ": " + jsonObj.message + "\r\n";
        }catch(e){
            mesage = event.data + "\r\n";
        }
        messagesArea.value = messagesArea.value + message;
        messagesArea.scrollTop = messagesArea.scrollHeight;
    };
}

function disconnect () {
    chatClient.close();
}

function sendMessage() {
    var user = document.getElementById("userName").value.trim();
    if (user === "")
        alert ("Please enter your name!");

    var inputElement = document.getElementById("messageInput");
    var message = inputElement.value.trim();
    if (message !== "") {
        var jsonObj = {"user" : user, "message" : message};
        chatClient.send(JSON.stringify(jsonObj));
        inputElement.value = "";
    }
    inputElement.focus();
}

function keydownOnMessage(event){
    var kc = event.which || event.keyCode;
    if (kc == 13) {
        sendMessage();
    }
}