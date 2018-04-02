# Cordova Homekey Interception Plugin
Homekey Interception plugin for Cordova

## Ionic
```
ionic cordova plugin add https://github.com/modraedlau/cordova-plugin-homekey-interception.git
```

## Example
```
ionic start MyIonicProject tabs
cd MyIonicProject
ionic cordova plugin add https://github.com/modraedlau/cordova-plugin-homekey-interception.git
```
### home.ts
```
import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

declare let cordova: any;

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(public navCtrl: NavController) {
      cordova.plugins.HomekeyInterception.register();
  }
}
```
### build
add android platform
```
ionic cordova platform add android
```
build android
```
ionic cordova build android
```