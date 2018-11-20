/** @format */

import React, { Component } from 'react';
import { AppRegistry, StyleSheet, Text, View } from 'react-native';

import StackNav from './StackNav';

import {name as appName} from './app.json';

export default class Mobile extends Component {
    render() {
        return (
            <StackNav navigation={this.props.navigation} />
        )
    }
}
AppRegistry.registerComponent(appName, () => Mobile);
// https://react-native-training.github.io/react-native-elements/docs/0.19.1/lists.html
// https://github.com/franciskone/react-native-redux-logic-boilerplate/
// https://facebook.github.io/react-native/docs/running-on-device
// https://medium.com/@rintoj/react-native-with-typescript-40355a90a5d7
// https://redux.js.org/basics