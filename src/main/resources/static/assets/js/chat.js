
    <script th:inline="javascript">
        let currentUser = /*[[${currentUser}]]*/ 'User';
        let chattingWith = null;
        let stompClient = null;
        let urlParams = new URLSearchParams(window.location.search);
        let preselectedRoomId = urlParams.get('roomId')
        function connect(roomName) {
            if (stompClient) {
                stompClient.disconnect();
            }
            const socket = new SockJS('/ws-chat');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                if (chattingWith) {
                    let roomId = generateRoomId(currentUser, chattingWith);
                    stompClient.subscribe('/topic/chatroom/' + roomId, function (message) {
                        let msg = JSON.parse(message.body);
                        addMessage(msg.sender, msg.content);
                    });
                }
            });
        }

        function generateRoomId(user1, user2) {
            return [user1, user2].sort().join('_');
        }

        function loadRooms() {
            fetch('/chat/rooms')
                .then(res => res.json())
                .then(data => {
                    const ul = document.getElementById('roomList');
                    ul.innerHTML = '';
                    data.forEach(roomUser => {
                        const li = document.createElement('li');
                        li.textContent = roomUser;
                        li.style.cursor = 'pointer';
                        li.onclick = () => selectRoom(roomUser);
                        ul.appendChild(li);
                    });
                });
        }

        function selectRoom(user) {
            chattingWith = user;
            document.getElementById('chattingWith').textContent = user;
            document.getElementById('messages').innerHTML = '';
            document.getElementById('inputBox').style.display = 'block';

            loadChatHistory(user);
            connect();
        }

        function loadChatHistory(user) {
            fetch(`/chat/history/${encodeURIComponent(user)}`)
                .then(res => res.json())
                .then(data => {
                    const messagesDiv = document.getElementById('messages');
                    messagesDiv.innerHTML = '';
                    data.forEach(msg => addMessage(msg.sender, msg.content));
                });
        }

        function addMessage(sender, content) {
            const messagesDiv = document.getElementById('messages');
            const p = document.createElement('p');
            p.textContent = sender + ": " + content;
            messagesDiv.appendChild(p);
            messagesDiv.scrollTop = messagesDiv.scrollHeight;
        }

        function sendMessage() {
            const input = document.getElementById('messageInput');
            const content = input.value.trim();
            if (content && stompClient && chattingWith) {
                const message = {
                    sender: currentUser,
                    recipient: chattingWith,
                    content: content
                };
                stompClient.send("/app/message", {}, JSON.stringify(message));
                input.value = '';
            }
        }

        window.onload = () => {
            loadRooms();
            console.log('Preselected Room ID:', preselectedRoomId);
            console.log('Current User:', currentUser);

            preselectedRoomId = decodeURIComponent(preselectedRoomId); // Giải mã UTF-8

            console.log("Decoded preselectedRoomId:", preselectedRoomId);

            if (preselectedRoomId) {
                preselectedRoomId = decodeURIComponent(preselectedRoomId.replace(/\+/g, ' '));
                console.log("Decoded roomId:", preselectedRoomId);

                let parts = preselectedRoomId.split('_');
                let other = parts.find(p => p !== currentUser);

                if (other) {
                    selectRoom(other);
                }
            }
        };
    </script>