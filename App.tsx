/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React,{useRef, useEffect} from 'react';
import {
  StyleSheet,
  Text,
  useColorScheme,
  View,
  Image,
  Animated
} from 'react-native';
import 'react-native-gesture-handler';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import {RootStackParamList} from './src/screens/RootStackParamList';
import Home from './src/screens/Home';
import Splash from './src/screens/Splash';
const drivingApp =  require('./src/assets/driving.png')

const Stack = createStackNavigator();


function App(): JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';
  const fadeAnim = useRef(new Animated.Value(0)).current; // Initial value for opacity: 0


  useEffect(() => {
    Animated.timing(fadeAnim, {
      toValue: 1,
      duration: 10000,
      useNativeDriver: true,
    }).start(() => {

    });
  }, [fadeAnim]);

  return (
    <NavigationContainer>
      <Stack.Navigator
      screenOptions={{
        headerShown: false
      }}
        initialRouteName='Splash'>
      <Stack.Screen name='Home' component={Home} />
      <Stack.Screen name='Splash' component={Splash} />
      
        
      </Stack.Navigator>
    </NavigationContainer>
  //   <View style={styles.container}>
  //   <Animated.View
  //   style={{
  //     alignItems:'center',
  //     opacity: fadeAnim, // Bind opacity to animated value
  //   }}>
  //     <Image source={drivingApp}/>
  //     <Text style={styles.label}>
  //         Driving App
  //     </Text>
  //   </Animated.View>
  // </View>
  );
}

const styles = StyleSheet.create({

    container: {flex: 1, alignItems: 'center', justifyContent: 'center',backgroundColor: '#EEEEEE'},
    sub_container: {alignItems:'center', justifyContent:'center'},
    label: {fontSize: 28, fontWeight: 600, color:'#000000'}
});

export default App;
