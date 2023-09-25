<!-- eslint-disable no-undef -->
<template>
    <div class="backIntegralmente">
        <div class="grdUser">
            <q-select outlined v-model="selProf" :options="appStore.state.users" label="sel topic" option-value="topic" option-label="name" emit-value map-options @update:model-value="onSelProfChange">
                <template v-slot:prepend>
                    <q-icon name="person" />
                </template>
                <template v-if="selProf" v-slot:append>
                    <q-icon name="cancel" @click.stop.prevent="selProf = null" class="cursor-pointer" />
                </template>
            </q-select>
            <div v-if="selProf">
                <q-btn color="primary" @click="cmd('gps')" class="btn">GPS</q-btn>
                <!--<q-btn @click="cmd('tracker')" class="btn">Tracker {{ command.tracker }}</q-btn>
            <q-btn @click="cmd('rec')" class="btn">Rec {{ command.rec }}</q-btn>
            <q-btn @click="cmd('play')" class="btn">Play {{ command.play }}</q-btn>-->
            </div>
        </div>

        <!--<q-select outlined v-model="selTrackDate" :options="appStore.state.userDates" label="sel date" option-value="id" option-label="id" emit-value map-options @update:model-value="onSelTrackDate">
            <template v-slot:prepend>
                <q-icon name="calendar" />
            </template>
            <template v-if="selProf" v-slot:append>
                <q-icon name="cancel" @click.stop.prevent="selProf = null" class="cursor-pointer" />
            </template>
        </q-select>-->
        <div ref="myMap" class="mapFrame"></div>
        <!--<div v-if="appStore.state.currentTrack?.[0]?.time">
            <q-slider class="sliderTrack" v-model="idx" @update:model-value="onSliderChange" :max="appStore.state.currentTrack?.length" :step="1" label :label-value="moment(appStore.state.currentTrack?.[idx]?.time).format('HH:mm:ss')" label-always color="blue" thumb-size="35px" />
        </div>-->
    </div>
</template>

<script setup>
/* eslint no-undef: 'off' */
import { ref, onMounted, watch } from 'vue'
import appStore from 'src/pages/appStore'
import { Loader } from '@googlemaps/js-api-loader'
import moment from 'moment'

const selProf = ref('')
const selTrackDate = ref()

const API_KEY = 'AIzaSyDvirhx5N6mi3ki2C_pFumye5bp8f_mBlw'
const loader = new Loader({ apiKey: API_KEY })
const myMap = ref(null)
const map = ref()
const idx = ref(0)
const command = ref({
    tracker: 'off',
    rec: 'off',
    play: 'off'
})

let guardPath
let markerStart
let markerEnd
let infowin

onMounted(async () => {
    await appStore.actions.init()
    await loader.load()
    map.value = new google.maps.Map(myMap.value, {
        center: new google.maps.LatLng(appStore.state.config.lat, appStore.state.config.lng),
        zoom: 14,
        disableDefaultUI: true
    })
})
const cmd = (c) => {
    if (command.value[c] === 'off') {
        command.value[c] = 'on'
    } else {
        command.value[c] = 'off'
    }
    const o = {
        action: command.value[c]
    }
    const args = JSON.stringify(o)
    appStore.actions.command(selProf.value, c, args)
    if (c === 'gps') {
        setTimeout(async () => {
            const loc = await appStore.actions.getLocation(selProf.value)
            drawGuard(loc)
        }, 1000)
    }
}
const onSelProfChange = async (e) => {
    console.log('onSelProfChange:', e)
    appStore.actions.getUserDates(e)
}
const onSelTrackDate = async (e) => {
    console.log('onSelTrackDate:', e)
    await appStore.actions.getCurrentTrack(selProf.value, e)
    drawTracks()
}
const drawTracks = () => {
    if (map.value && appStore.state.currentTrack.length > 0) {
        console.log('drawTracks: ', appStore.state.currentTrack.length)
        drawStart()
        drawPathTrack()
        drawGuard()
    }
}
const drawStart = () => {
    if (!markerStart) {
        const image = {
            url: 'pushpin.svg',
            // size: new google.maps.Size(40, 40),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(0, 25),
            scaledSize: new google.maps.Size(25, 25)
        }

        const pStart = appStore.state.currentTrack[0]
        markerStart = new google.maps.Marker({
            position: pStart,
            icon: image,
            map: map.value,
            animation: google.maps.Animation.DROP,
            title: 'Usted salió de aquí.'
        })
        map.value.setCenter(pStart)
    }
}
const drawPathTrack = () => {
    guardPath?.setMap(null)
    const pos = appStore.state.currentTrack.slice(0, idx.value + 1)
    guardPath = new google.maps.Polyline({
        path: pos,
        strokeColor: '#007aaa', // '#11a211', // '#007aff',
        strokeWeight: 5,
        map: map.value
    })
    guardPath.setMap(map.value)
}
const drawGuard = (loc) => {
    const pEnd = appStore.state.currentTrack[idx.value] || loc
    markerEnd?.setMap(null)
    const image = new google.maps.MarkerImage('security.svg', null, null, null, new google.maps.Size(40, 40))
    markerEnd = new google.maps.Marker({
        icon: image,
        position: pEnd, // new google.maps.LatLng(p.lat, p.lng),
        map: map.value,
        animation: google.maps.Animation.DROP,
        id: appStore.state.currentTrack.userName,
        title: 'Usted está aquí.'
    })
    markerEnd.addListener('click', async () => {
        console.log('markerEnd addListener.....')
        map.value.setCenter(markerEnd.getPosition())
        const contHtml = `
                <div >
                    <div>(X)</div>
                </div>
            `
        evalCloseInfo()
        infowin = new google.maps.InfoWindow({
            content: contHtml
        })
        infowin.open(map.value, markerEnd)
    })
    map.value.setCenter(pEnd)
}
const resetMap = () => {
    console.log('resetMap')
    idx.value = 0

    guardPath?.setMap(null)
    guardPath = undefined

    markerStart?.setMap(null)
    markerStart = undefined

    markerEnd?.setMap(null)
    markerEnd = undefined
}
const onSliderChange = (index) => {
    drawTracks()
}

// watch(() => appStore.state.currentDate, (newVal, oldVal) => {
//    console.log('watch map currentDate changes', newVal)
//    resetMap()
//    // if (newVal?.startTime !== oldVal?.startTime) {
//    //    console.log('changing current Date reset =>currentDate', newVal)
//    //    resetMap()
//    // }
//    idx.value = appStore.state.currentTrack.length - 1
//    drawTracks()
// })

</script>

<style scoped>
.grdUser {
    display: grid;
    grid-template-columns: 1fr 125px;
    align-items: center;
    padding: 4px;
    column-gap: 10px;
}

.btn {
    width: 120px;
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

.imgBack {
    position: relative;
    padding-top: 100px;
    height: 100vh;
    left: -7vw;
    width: 110vw;
    opacity: .1;
    object-fit: cover;
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

.sliderTrack {
    position: absolute;
    bottom: 5px;
}

.mapFrame {
    height: calc(100vh - 58px);
    width: 100%;
}
</style>
