<template>
    <div class="cardFrame" @click="callRing">
        <img :src="`images/${data.photo}.png`" class="photo" />
        <div>
            <div class="name">{{ data.name }}</div>
            <div class="profession">{{ data.profession }}</div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps(['data'])

</script>

<style scoped>
.cardFrame {
    border-radius: 10px;
    box-shadow: 1px 1px 4px black;
    padding: 5px;
    display: grid;
    grid-template-columns: 15vw 1fr;
    width: 70%;
    align-items: center;
    margin: 4vw auto;
    background: lightgray;
}

.cardFrame:active {
    box-shadow: inset 1px 1px 5px -1px rgba(0, 0, 0, 0.6);
}

.photo {
    border-radius: 50%;
    box-shadow: 0.5vw 0.5vw 0.5vw grey;
    margin-left: 2vw;
    height: 8vw;
    width: 8vw;
}

.name {
    font-size: 3.5vw;
    font-weight: bold;
}

.profession {
    font-size: 3vw;
}

@media screen and (min-width: 600px) {
    .cardFrame {
        grid-template-columns: 90px 1fr;
        width: 420px;
        margin: 20px auto;
    }

    .name {
        font-size: 21px;
        font-weight: bold;
    }

    .profession {
        font-size: 18px;
    }

    .photo {
        border-radius: 50%;
        box-shadow: 1px 1px 3px grey;
        margin-left: 13px;
        height: 50px;
        width: 50px;
    }
}
</style>
