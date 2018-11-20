import React, { Component } from 'react';
import { StyleSheet, View, Text } from 'react-native';
import { List, ListItem } from 'react-native-elements'
import { TouchableScale } from 'react-native-touchable-scale';
import Moment from 'moment';


export default class MainForm extends Component {
    constructor(props) {
        super(props);
    }

    openEvent = (idEvent) => {
        console.log(idEvent);
    }

    render (){
        return (
        <View style={styles.container}>
            <Text>Appuntamenti</Text>
            <List>
            {
                this.props.events.map((item) => (
                    <ListItem
                        key={item.idEvent}
                        title={item.companyName}
                        subtitle={Moment(item.calendarDate).format('DD/MM HH:mm')}
                        // rightIcon={{name: item.actionId == 1 ? 'phone' : 'person'}}
                        leftIcon={{name: 'phone'}}
                        onPress={this.openEvent(item.idEvent)}
                        chevronColor="white"
                        chevron
                    />
                ))
            }
            </List>
        </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        padding: 20
    }
});


