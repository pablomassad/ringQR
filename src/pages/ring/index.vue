<template>
    <div class="backIntegralmente">
        <img src="images/Integralmente.jpeg" class="imgBack" />
        <div class="headerFrame">
            <img src="images/EspTerapeutico.jpg" class="imgHeader" />
        </div>
        <div class="profList">
            <Card v-for="info in appStore.state.staff" :key="info.topic" :data="info" @click="onProfSelected(info.topic)" />
            <div class="address">
                San Lorenzo 186 / Pergamino
            </div>
        </div>
        <div class="hiddenPAM" v-touch-hold.mouse="ringPAM"></div>

        <ConfirmDialog :prompt="showNoti" no-persistant @onClose="showNoti = false">
            <template #header>
                <div class="row justify-between title">
                    Bienvenido a INTEGRALMENTE
                </div>
            </template>
            <template #default>
                <div style="margin:10vw">
                    <q-input filled v-model="nombre" label="Ingresa tu nombre" :input-style="{fontSize: '5vw'}"></q-input>
                </div>
            </template>
            <template #footer>
                <q-btn class="btnFooter" color="blue-8" @click="ring" size="lg">TOCAR TIMBRE</q-btn>
                <div class="waitFrame">
                    Presiona TOCAR TIMBRE y en breve serás atendido por uno de nuestros profesionales
                </div>
            </template>
        </ConfirmDialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import appStore from 'src/pages/appStore'
import Card from 'src/pages/ring/Card.vue'
import ConfirmDialog from 'fwk-q-confirmdialog'
import { LocalStorage } from 'quasar'
import moment from 'moment'

const showNoti = ref(false)
const selProf = ref('')
const nombre = ref(LocalStorage.getItem('QR_name'))

onMounted(async () => {
    await appStore.actions.init()
})
const onProfSelected = (topic) => {
    showNoti.value = true
    selProf.value = topic
}
const ring = () => {
    showNoti.value = false
    const args = {
        nombre: nombre.value,
        hora: moment().format('HH:mm:ss')
    }
    LocalStorage.set('QR_name', nombre.value)
    appStore.actions.command(selProf.value, 'ring', JSON.stringify(args))
    const plRing = { ...args, dest: selProf.value, dia: moment().format('DD/MM/YY') }
    appStore.actions.logRing(plRing)
    const audio = new Audio('https://pp-ringqr.web.app/ring.mp3')
    audio.play()
}
const ringPAM = () => {
    showNoti.value = true
    selProf.value = 'PAM'
}
const close = () => {
    showNoti.value = false
}
</script>

<style scoped>
.imgBack {
    position: relative;
    padding-top: 100px;
    min-height: 1000px;
    height: 100vw;
    left: -6vw;
    width: 111vw;
    opacity: .1;
    object-fit: cover;
}

.title {
    font-size: 7vw;
    text-shadow: 1px 1px 1px gray;
    font-weight: bold;
    text-align: center;
}

.waitFrame {
    margin: 10px;
    font-size: 3vw;
    text-align: center;
}

.btnFooter {
    width: 90%;
    margin: auto;
}

.address {
    text-align: center;
    font-size: 4vw;
    font-weight: bold;
}

.profList {
    position: absolute;
    top: 110px;
    width: 100%;
}

.backIntegralmente {
    background: white;
    width: 100vw;
    height: 100vh;
}

.headerFrame {}

.imgHeader {
    position: absolute;
    top: 0;
    right: 0;
    left: -15px;
    margin: auto;
    width: 220px;
    max-width: 500px;
}

.btnRing {
    position: absolute;
    top: 10%;
    right: 0;
    left: 0;
    margin: auto;
    height: 200px;
    width: 200px;
    border-radius: 50%;
    box-shadow: 2px 2px 10px black;
    border: 2px solid black;
    background: rgb(56, 89, 197);
    color: white;
}

.txtRing {
    text-shadow: 1px 1px 3px black;
    font-size: 20px;
    font-weight: bold;
}

@media screen and (min-width: 600px) {

    .profList {
        width: 600px;
        left: 0;
        right: 0;
        margin: auto;
    }

    .title {
        font-size: 43px;
        place-content: space-around;
    }

    .waitFrame {
        font-size: 18px;
    }

    .address {
        font-size: 25px;
    }
}

.hiddenPAM {
    position: absolute;
    background-color: transparent;
    width: 40px;
    height: 40px;
    top: 0;
    right: 0;
}
</style>
