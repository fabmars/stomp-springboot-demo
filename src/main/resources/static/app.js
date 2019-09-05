var stompClient = null;
var playerId;
var gameStarted;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);

    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gamin-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        // starts with /user because the server replies to the caller only
        stompClient.subscribe('/user/topic/join', function (message) {
            var playerJoinedMessage = JSON.parse(message.body);
            playerId = playerJoinedMessage.playerId;

            if (playerJoinedMessage.currentQuestion) {
                showQuestion(playerJoinedMessage.currentQuestion);
            }

            subscribeToQuestions();
        });

        stompClient.subscribe('/topic/status', function (message) {
            var playerCountMessage = JSON.parse(message.body);
            $("#playerCount").html(playerCountMessage.playerCount + " players.");
        });
    });
}

function subscribeToQuestions() {
    stompClient.subscribe('/topic/question', function (message) {
        var ackOrQuestionMessage = JSON.parse(message.body);
        if (ackOrQuestionMessage.pleaseWait) {
            $("#answerCount").html(ackOrQuestionMessage.answerCount + " answers...");
        } else {
            $("#answerCount").empty();
            showQuestion(ackOrQuestionMessage);
        }
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendJoin() {
    stompClient.send("/app/join", {}, "{}");
}

function sendStart() {
    stompClient.send("/app/start", {}, "{}");
    $("#start").prop("disabled", true);
}

function sendAnswer(answer) {
    stompClient.send("/app/answer", {}, JSON.stringify({'answer': answer.value, 'playerId': playerId}));
}

function showQuestion(message) {
    if (!gameStarted) {
        gameStarted = true;
        $("#start").prop("disabled", true); // again, because it's shown for every player
    }

    $("#question").html(message.question);

    $("#answers").empty();
    for (answerId in message.answers) {
        $("#answers").append("<input type=\"button\" id=\"answer" + answerId + "\" class=\"btn btn-warning col\" value=\"" + message.answers[answerId] + "\" onclick=\"sendAnswer(this);\"/>")
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function() { connect(); });
    $("#disconnect").click(function() { disconnect(); });
    $("#join").click(function() { sendJoin(); });
    $("#start").click(function() { sendStart(); });
});