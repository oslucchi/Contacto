import React, { Component } from 'react';
import { StyleSheet, View, TextInput, TouchableOpacity, Text, StatusBar, AsyncStorage } from 'react-native';

export default class LoginForm extends Component {
    constructor(props) {
        // Required step: always call the parent class' constructor
        super(props);

        // Set the state directly. Use props if necessary.
        this.state = {
            email: "",
            password: ""
        }
    }

    _onEmailTextChanged = (event) => {
        this.setState({ email: event.nativeEvent.text });
    };

    _onPasswordTextChanged = (event) => {
        this.setState({ password: event.nativeEvent.text });
    };


    render() {
        return (
            <View style={styles.container}>
                <StatusBar
                    barStyle="light-content"
                />
                <TextInput
                    placeholder="email"
                    placeholderTextColor="rgba(220,220,220,0.7)"
                    returnKeyType="next"
                    onSubmitEditing={() => this.passwordInput.focus()}
                    keyboardType="email-address"
                    autoCapitalize="none"
                    autoCorrect={false}
                    style={styles.input}
                    onChange = {this._onEmailTextChanged}
                    ref={(input) => this.emailInput = input}
                />
                <TextInput
                    placeholder="password"
                    placeholderTextColor="rgba(220,220,220,0.7)"
                    returnKeyType="go"
                    secureTextEntry
                    style={styles.input}
                    ref={(input) => this.passwordInput = input}
                    onChange = {this._onPasswordTextChanged}
                />

                <TouchableOpacity
                    style={styles.buttonContainer}
                    onPress={()=>{
                        this.props.onLogin(this.state.email, this.state.password)
                    }} >
                    <Text style={styles.buttonText}>LOGIN</Text>
                </TouchableOpacity>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        padding: 20
    },
    input: {
        height: 40,
        width:250,
        backgroundColor: 'rgba(32,32,32,0.4)',
        marginBottom: 10,
        color: '#FFF',
        paddingHorizontal: 10
    },
    buttonContainer: {
        backgroundColor: '#2980B9',
        paddingVertical: 15
    },
    buttonText: {
        textAlign: 'center',
        color: '#FFFFFF',
        fontWeight: '700'
    }
});


