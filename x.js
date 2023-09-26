const fs = require('fs')
const path = require('path')
const execSync = require('child_process').execSync
const professionals = require('./src/pages/professionals.json')

// eslint-disable-next-line no-extend-native
String.prototype.lpad = function (padString, length) {
    let str = this
    while (str.length < length) str = padString + str
    return str.valueOf()
}

updateVersion()
professionals.forEach(usr => {
    updateUser(usr.topic)
    buildApk()
    deployApk(usr.topic)
})

function updateVersion () {
    const bufVer = fs.readFileSync(path.join(__dirname, '/android-RingQR/app/src/main/java/com/pp/ringqr/RingQRApp.kt'))
    let strVer = bufVer.toString()

    let newVersionName$
    const regVer = /(([0-9]{1}.[0-9]{1,2}).([0-9]{2}))/gm
    strVer = strVer.replace(regVer, ($0, $1, $2, $3) => {
        // console.log('$0', $0) // 1.01.02
        // console.log('$1', $1) // 1.01.02
        // console.log('$2', $2) // 1.01
        // console.log('$3', $3) // 02
        const newBuildNum = Number($3) + 1
        const buildNum$ = newBuildNum.toString().lpad('0', 2)
        console.log('incrementa build num:', buildNum$)
        newVersionName$ = $2 + '.' + buildNum$

        const match = newVersionName$
        return match
    })
    fs.writeFileSync(path.join(__dirname, '/android-RingQR/app/src/main/java/com/pp/ringqr/RingQRApp.kt'), strVer, err => {
        if (err === null) {
            console.log('Actualizacion RingQRApp.kt => OK')
        } else {
            console.log('Error actualizando RingQRApp.kt: ', err)
        }
    })

    console.log('')
    console.log('')
    console.log('#########################')
    console.log('New Version ' + newVersionName$)
    console.log('#########################')
    console.log('')
}
function updateUser (name) {
    const bufUser = fs.readFileSync(path.join(__dirname, '/android-RingQR/app/src/main/java/com/pp/ringqr/RingQRApp.kt'))
    let strUser = bufUser.toString()

    const regEnvId = /( var topicname: String = )(.\w*")/gm
    strUser = strUser.replaceAll(regEnvId, ($0, $1, $2) => {
        // console.log('$0:', $0)
        // console.log('$1:', $1)
        // console.log('$2:', $2)
        const match = $1 + `"${name}"`
        console.log('new Name => ' + match)
        return match
    })

    console.log('Actualiza RingQRApp')
    fs.writeFileSync(path.join(__dirname, '/android-RingQR/app/src/main/java/com/pp/ringqr/RingQRApp.kt'), strUser, err => {
        if (err === null) {
            console.log('Actualizacion RingQRApp OK')
        } else {
            console.log('Error actualizando RingQRApp: ', err)
        }
    })
}
function buildApk () {
    /// /////////////////////////////////////////////////////////////////////////////
    // GENERA nueva APK
    /// /////////////////////////////////////////////////////////////////////////////
    console.log('Genera nueva apk')
    console.log('...............................')

    process.chdir('android-RingQR')

    // execSync('./gradlew assembleRelease', {
    //    stdio: 'inherit'
    // })
    execSync('./gradlew assembleDebug', {
        stdio: 'inherit'
    })
    process.chdir('..')
}
function deployApk (name) {
    console.log('')
    console.log('')
    console.log('######################')
    console.log('DEPLOY TO MyDrive')
    console.log('######################')

    /// /////////////////////////////////////////////////////////////////////////////
    // COPIA APK al destino
    /// /////////////////////////////////////////////////////////////////////////////
    const dest = `/Users/pablin/My Drive/PP/P♡P/P&P Soft/RingQR/ring_${name}.apk`
    console.log('Copiando apk a: ', dest)
    fs.copyFileSync(
        './android-RingQR/app/build/outputs/apk/debug/app-debug.apk',
        dest
    )

    console.log('##############')
    console.log('FIN SCRIPT APK')
    console.log('##############')
}
