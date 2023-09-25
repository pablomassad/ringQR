<template>
    <div>
        <div class="navbar" @click="toggleChat"></div>
        <div class="mainFrame">
            <div class="videoFrame"></div>
            <div class="chatFrame" v-if="showChatFrame">
                <div class="chatMain">
                    <div class="msg">PRIMERA LINEA......</div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>

                    <div class="msg">Todo bien por aca</div>
                    <div class="msg">Todo bien por aca</div>
                    <div class="msg">Todo bien por aca</div>
                    <div class="msg">Todo bien por aca</div>
                    <div class="msg">Todo bien por aca</div>
                    <div class="msg">Todo bien por aca</div>
                    <div class="msg">Todo bien por aca</div>
                    <div class="msg">Todo bien por aca</div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">
                        Hola mundo como viene toolbox laburando en telecom
                    </div>
                    <div class="msg">ANTE ULTIMA LINEA........</div>
                    <div class="msg">ULTIMA LINEA........</div>
                </div>
                <div class="chatFooter">
                    <div class="footerGrid">
                        <button>H</button>
                        <input type="text" />
                        <button>=></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'

const showChatFrame = ref(true)

const toggleChat = () => {
    showChatFrame.value = !showChatFrame.value
}
</script>

<style scoped>
.mainFrame {
    position: absolute;
    background: red;
    width: 100%;
    top: 0;
    bottom: 0;
}

.navbar {
    position: relative;
    background: cyan;
    width: 100%;
    height: 50px;
    z-index: 100;
    opacity: 0.7;
}

.videoFrame {
    position: absolute;
    width: 100%;
    top: 50px;
    right: 0;
    bottom: 0;
    background: violet;
}

.chatFrame {
    position: absolute;
    width: 40%;
    top: 50px;
    right: 0;
    bottom: 0;
    background: yellow;
}

.chatMain {
    position: absolute;
    overflow-y: scroll;
    top: 0;
    bottom: 50px;
}

.msg {
    position: relative;
    background: lightblue;
    padding: 10px;
    margin: 5px;
    border-radius: 5px;
}

.chatFooter {
    position: absolute;
    background: green;
    width: 100%;
    max-width: 500px;
    height: 50px;
    bottom: 0;
    text-align: left;
    align-self: center;
}

.footerGrid {
    background: gray;
    display: grid;
    grid-template-columns: 30px 1fr 40px;
    padding: 7px;
}</style>
