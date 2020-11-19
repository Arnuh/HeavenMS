importPackage(Packages.client);
importPackage(Packages.tools);
importPackage(Packages.server.life);

var towns = new Array(82042, 82044, 82290, 82291);
var spawns = new Array(8210012, 8210012, 8210011, 8210011);
var x = new Array(1000, 1000, -350, 1150);
var y = new Array(-65, -65, -500, -500);
var mapObj;
var mobObj;

function init() {
    scheduleNew();
}

function scheduleNew() {
    setupTask = em.schedule("start", 0);
}

function cancelSchedule() {
    if (setupTask != null)
        setupTask.cancel(true);
}

function start() {
    for (var i = 0; i < towns.length; i++) {
        mapObj = em.getChannelServer().getMapFactory().getMap(towns[i]);
        mobObj = Packages.server.life.MapleLifeFactory.getMonster(spawns[i], mapObj.getChannelId());
        if (mapObj.getMonsterById(spawns[i]) == null) {
            mapObj.spawnMonsterOnGroundBelow(mobObj, new Packages.java.awt.Point(x[i], y[i]));
        }
    }
    setupTask = em.schedule("start", Randomizer.rand(5, 10) * 60000);
}

// ---------- FILLER FUNCTIONS ----------

function dispose() {
}

function setup(eim, leaderid) {
}

function monsterValue(eim, mobid) {
    return 0;
}

function disbandParty(eim, player) {
}

function playerDisconnected(eim, player) {
}

function playerEntry(eim, player) {
}

function monsterKilled(mob, eim) {
}

function scheduledTimeout(eim) {
}

function afterSetup(eim) {
}

function changedLeader(eim, leader) {
}

function playerExit(eim, player) {
}

function leftParty(eim, player) {
}

function clearPQ(eim) {
}

function allMonstersDead(eim) {
}

function playerUnregistered(eim, player) {
}