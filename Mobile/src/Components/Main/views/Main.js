'use strict';

import React, { Component } from 'react';
import { StyleSheet, Text, View, AsyncStorage } from 'react-native';

import MainForm from "./components/MainForm"

const GLOBAL = require('../../../../Globals');

export default class Main extends Component {
    constructor (props) {
        super(props);
        this.state = {
            events: []
        };
        AsyncStorage.getItem('token').then(token => {
            fetch(GLOBAL.BASE_URL + '/event',
                {
                    method: 'POST',
                    headers: new Headers({
                        'Authorization': token,
                        'Content-Type': 'application/json',
                        'Language': 'it_IT'
                    })
                }
            )
            .then(response => {
                const status = response.status;
                response.json()
                .then(jsonResponse => {
                    this.setState({events: jsonResponse.events});
                    console.log(this.state.events)
                })
                .catch(error=>{
                    console.error(error);
                })
            })
            .catch(error => {
                console.log(error);
            });
        })
    }

    render (){
        if (!this.state.events) return null;
        return (
            <MainForm events={this.state.events} />
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#ff8c00'
    }
})