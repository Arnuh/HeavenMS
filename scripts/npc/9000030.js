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
                cm.sendSimple("Hi! Since you have no items to pick up, I am that one shop.#l\r\n\#L0#Potions#l\r\n\#L19#Cash Pets Acc#l\r\n\#L20#Ammo#l\r\n\#L21#Misc#l\r\n\#L22#Skill Panels#l");
            }
        }
    } else {
        cm.dispose();
        if (selection >= 0) {
            cm.openShopNPC(10055 + selection);
        }
    }
}


















