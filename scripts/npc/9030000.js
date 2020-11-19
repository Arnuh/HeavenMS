/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation version 3 as published by
 the Free Software Foundation. You may not use, modify or distribute
 this program under any other version of the GNU Affero General Public
 License.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/* Fredrick NPC (9030000)
 * @author kevintjuh93
 */

var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        if (!cm.hasMerchant() && cm.hasMerchantItems()) {
            cm.showFredrick();
            cm.dispose();
        } else {
            if (cm.hasMerchant()) {
                cm.sendOk("You have a Merchant open.");
                cm.dispose();
            } else {
                cm.sendSimple("Hi! Since you have no items to pick up, I am that one scroll shop.#l\r\n\#L0#70% Armor Scrolls#l\r\n\#L1#70% Weapon Scrolls#l\r\n\#L2#30% Armor Scrolls#l\r\n\#L3#30% Weapon Scrolls#l\r\n\#L4#15% Scrolls#l\r\n\#L5#NX Shop");
            }
        }
    } else if (status == 1) {
        if (selection == 5) {
            cm.sendSimple("Hi! I am that one NX shop.#l\r\n\#L0#Capes#l\r\n\#L1#Gloves#l\r\n\#L2#Shields#l\r\n\#L3#Rings#l\r\n\#L5#Eye Acc#l\r\n\#L6#Face Acc#l\r\n\#L7#Char Effects#l\r\n\#L8#Stars#l\r\n\#L10#Weapons#l\r\n\#L11#Hats#l\r\n\#L12#Overall#l\r\n\#L13#Shoes#l\r\n\#L14#Top#l\r\n\#L15#Bottom");
        } else {
            cm.dispose();
            cm.openShopNPC(10000 + selection);
        }
    } else {
        cm.dispose();
        cm.openShopNPC(10058 + selection);
    }
}


















