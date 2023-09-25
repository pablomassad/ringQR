<template>
    <div>
        <div class="toolbar">
            <q-btn color="primary" icon-right="add" @click="uploadPhoto" />
            <q-btn color="secondary" icon-right="save" @click="capturePhotox" />
        </div>
        <!--<canvas id="canvas"></canvas>-->
        <div class="contPhoto" id="draggableArea">
            <viewer>
                <img :src="picture?.data" class="framePhoto" />
            </viewer>
            <div class="localFrame" id="draggableItem">
                <video class="localCam" ref="videoLocalRef" autoPlay></video>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Camera, CameraResultType } from '@capacitor/camera'

import 'viewerjs/dist/viewer.css'
import VueViewer from 'v-viewer'

import gsap from 'gsap'
import Draggable from 'gsap/Draggable'
gsap.registerPlugin(Draggable)

console.log('CONSTRUCTOR photo')

const videoLocalRef = ref(null)
const picture = ref()

onMounted(() => {
    const supports = navigator.mediaDevices.getSupportedConstraints()
    console.log('Browser supports pan:', supports.pan)
    console.log('Browser supports tilt:', supports.tilt)
    console.log('Browser supports zoom:', supports.zoom)

    createDraggable()
    initCamera()
    permissions()
})

const createDraggable = () => {
    const arr = Draggable.create('#draggableItem', {
        type: 'x,y',
        bounds: '#draggableArea'
        // onPress:()=>console.log('drag press'),
    })
}
const initCamera = async () => {
    try {
        const stream = await navigator.mediaDevices.getUserMedia({
            audio: false,
            video: {
                pan: true,
                tilt: true,
                zoom: true,
                width: { min: 340, ideal: 1280, max: 1920 },
                height: { min: 240, ideal: 720, max: 1080 },
                deviceId: 'default'
                // facingMode: { exact: "environment" } || facingMode: 'user'
            }
        })
        // videoLocalRef.value = stream
        const vid = document.querySelector('video')
        vid.srcObject = stream
    } catch (error) {
        // User denies prompt or matching media is not available.
        console.log(error)
    }
}
const permissions = async () => {
    try {
        const panTiltZoomPermissionStatus = await navigator.permissions.query({
            name: 'camera',
            panTiltZoom: true
        })

        if (panTiltZoomPermissionStatus.state === 'granted') {
            console.log('granted persmission to PTZ')
        }

        panTiltZoomPermissionStatus.addEventListener('change', () => {
            console.log('user changed persmissions!')
        })
    } catch (error) {
        console.log(error)
    }
}
const uploadPhoto = async () => {
    console.log('uploadPicture....')
    const pic = await Camera.getPhoto({
        quality: 100,
        allowEditing: true,
        resultType: CameraResultType.Uri
    })
    const url = await fetch(pic.webPath)
    const blob = await url.blob()
    console.log('blob:', blob)
    const reader = new FileReader()
    reader.onload = () => {
        console.log('reader.result:', reader.result)
        const img = { filename: `imagen.${pic.format}`, data: reader.result }
        console.log('img:', img)
        picture.value = img
    }
    reader.readAsDataURL(blob)
}
const capturePhoto = () => {
    const canvas = document.getElementById('canvas')
    const context = canvas.getContext('2d')

    const div = document.getElementById('photo')
    context.drawImage(div, 0, 0, canvas.width, canvas.height)

    const img = canvas.toDataURL('image/png')

    const link = document.createElement('a')
    link.href = img
    link.download = 'captura.png'
    link.click()
}
</script>

<style scoped>
.toolbar {
    position: relative;
    width: 100%;
    height: 50px;
    background-color: cadetblue;
}

.contPhoto {
    position: absolute;
    width: 100%;
    top: 50px;
    bottom: 0;
    background-color: white;
}

.framePhoto {
    position: absolute;
    padding: 5px;
    object-fit: contain;
    width: 100%;
    top: 50px;
    bottom: 0;
}

.localFrame {
    position: absolute;
    will-change: transform;
    cursor: grabbing;
    overflow: hidden;
    z-index: 1000;
    background: transparent;
}

.localCam {
    width: 320px;
    box-sizing: border-box;
    -webkit-mask-image: radial-gradient(ellipse at center, rgba(0, 0, 0, 1) 0%, rgba(0, 0, 0, 1) 60%, rgba(0, 0, 0, 0) 70%);
    mask-image: radial-gradient(ellipse at center, rgba(0, 0, 0, 1) 0%, rgba(0, 0, 0, 1) 60%, rgba(0, 0, 0, 0) 70%);
}
</style>
