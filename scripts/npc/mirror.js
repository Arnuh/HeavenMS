
var status;
var sel;
var summon = 4006001;
var towns = new Array(100000000, 101000000, 102000000, 103000000, 120000000, 105040300, 200000200, 211000000, 230000000, 250000000, 251000000, 220000000, 221000000, 222000000, 260000000, 261000000, 240000000, 800000000, 600000000, 270000000, 271010000,241000120,310000000,310070000,400000000,401040000,402000000,402000500,402000600,450001000,450014050,450002000,450015020,450003000,450005000,450006130,450007040);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            var selStr = "";
                selStr += "#0# Henesys";
                selStr += "#1# Ellinia Forest";
                selStr += "#2# Perion";
                selStr += "#3# Kerning City";
                selStr += "#4# Nautilus Harbor";
                selStr += "#5# Sleepywood";
                if (cm.getPlayer().getTotalLevel() >= 30) {
                    selStr += "#6# Orbis";
                    selStr += "#9# Mu Lung";
                    selStr += "#10# Herb Town";
                }
                if (cm.getPlayer().getTotalLevel() >= 50) {
                    selStr += "#7# El Nath";
                    selStr += "#8# Aquarium";
                    selStr += "#11# Ludibrium";
                    selStr += "#17# Mushroom Shrine";
                }
                if (cm.getPlayer().getTotalLevel() >= 70) {
                    selStr += "#12# Omega Sector";
                    selStr += "#13# Korean Folk Town";
                    selStr += "#14# Ariant";
                    selStr += "#15# Magatia";
                }
                if (cm.getPlayer().getTotalLevel() >= 100) {
                    selStr += "#16# Leafre";
                    selStr += "#18# New Leaf City";
                    selStr += "#19# Temple of Time";
                }
				if (cm.getPlayer().getTotalLevel() >= 200) {
					selStr += "#20# Future Henesys";
				}
				if (cm.getPlayer().getTotalLevel() >= 300) {
					selStr += "#21# Kritias";
				}
				if (cm.getPlayer().getTotalLevel() >= 500) {
					selStr += "#22# Edelstin";
				}
				if (cm.getPlayer().getTotalLevel() >= 700) {
					selStr += "#23# Black Heaven";
					selStr += "#24# Pantheon";
				}
				if (cm.getPlayer().getTotalLevel() >= 800) {
					selStr += "#25# Tyrant Castle";
					selStr += "#26# Black Market";
				}
				if (cm.getPlayer().getTotalLevel() >= 900) {
					selStr += "#27# Santurary";
					selStr += "#28# Verdel";
				}
				if (cm.getPlayer().getTotalLevel() >= 1000) {
					selStr += "#29# Nameless Town";
				}
				if (cm.getPlayer().getTotalLevel() >= 1100) {
					selStr += "#30# Reverse City";
				}
				if (cm.getPlayer().getTotalLevel() >= 1200) {
					selStr += "#31# Chu Chu";
				}
				if (cm.getPlayer().getTotalLevel() >= 1300) {
					selStr += "#32# Yum Yum";
				}
				if (cm.getPlayer().getTotalLevel() >= 1400) {
					selStr += "#33# Lachelen";
				}
				if (cm.getPlayer().getTotalLevel() >= 1500) {
					selStr += "#34# Arcana";
				}
				if (cm.getPlayer().getTotalLevel() >= 1600) {
					selStr += "#35# Morass";
				}
				if (cm.getPlayer().getTotalLevel() >= 1800) {
					selStr += "#36# Esfera";
				}
                cm.sendDimensionalMirror(selStr);
        } else if (status == 1) {
            //cm.getPlayer().saveLocation("MIRROR");
            cm.warp(towns[selection], 0);
            cm.dispose();
        }
    }
}













