import { createStackNavigator } from 'react-navigation';
import Login from './src/Components/Auth/Login';
import Main from './src/Components/Main';

export default createStackNavigator(
  {
    Login: { screen: Login },
    Main: { screen: Main },
  },
  {
    initialRouteName: 'Login'
  });