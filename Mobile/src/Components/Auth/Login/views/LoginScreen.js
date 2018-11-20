'use strict';

import React, { Component } from 'react';
import { StyleSheet, View, Text, Image, KeyboardAvoidView, AsyncStorage } from 'react-native';
import LoginForm from './components/LoginForm';

const GLOBAL = require('../../../../../Globals');

export default class Login extends Component  {
    constructor (props) {
        super(props);
        this.state = {
            message: ""
        };
        AsyncStorage.getItem('token').then(token => {
            console.log(`token: ${token}`);
            if ((token !== null) && (token !== "")) {
                fetch(GLOBAL.BASE_URL + '/auth/loginByToken',
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
                        .then(jsonResponse => this.handleResponse(status, jsonResponse))
                        .catch(error=>{
                            console.error(error);
                        })
                    })
                    .catch(error => {
                        console.log(error);
                    });
            }
        });   
       
    }

    handleResponse = (status, jsonResponse) => {
        if (status === 200) {
            AsyncStorage.setItem('token', jsonResponse.token).then(res => {
                this.props.navigation.navigate('Main');
            }).catch(err=>{
                console.error(err);
            });
        } else {
            this.setState({ message: `${jsonResponse.error}` });
        }
    }

    onLoginPressed = (email, password) => {
        console.log(email, password)
        const authData = {
            email, 
            password
        }

        fetch(GLOBAL.BASE_URL + '/auth/login',
                {
                    method: 'POST',
                    headers: new Headers({
                        'Content-Type': 'application/json',
                        'Language': 'it_IT'
                    }),
                    body: JSON.stringify(authData)
                }
            )
            .then(response =>  {
                const status = response.status;
                response.json()
                .then(jsonResponse => this.handleResponse(status, jsonResponse))
                .catch(error=>{
                    console.error(error);
                })
            })
            .catch(error=>{
                console.error(error);
            });
    };

    render() {
        return (
            <View style={styles.logoContainer}>
                <Image
                    style={styles.logo}
                    source={require('../../../../Images/Logo.png')}
                />
                <Text style={styles.title}>Your reports in good hands</Text>
                <LoginForm onLogin={this.onLoginPressed}/>
                <Text style={styles.errorContainer}>{this.state.message}</Text>
            </View>
        );
    }
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#ff8c00'
    },
    logoContainer: {
        alignItems: 'center',
        flexGrow: 1,
        justifyContent: 'center'
    },
    logo: {
        width: 195,
        height: 28
    },
    title: {
        color: '#000000',
        marginTop: 10,
        width: 160,
        textAlign: 'center',
        opacity: 0.8
    },
    errorContainer: {
        alignItems: 'center',
        width: 250,
        flexDirection:"row",
        color: 'rgba(244, 155, 66, 0.7)',
        justifyContent: 'center'
    },
    formContainer: {
    }
});
