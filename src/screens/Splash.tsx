import { useNavigation } from '@react-navigation/native';
import React,{useRef, useEffect} from 'react'
import {View, Text, Image, StyleSheet, Animated} from 'react-native'

const Splash = () => {
const fadeAnim = useRef(new Animated.Value(0)).current; // Initial value for opacity: 0
const drivingApp =  require('../assets/driving.png')
const navigation = useNavigation();
  useEffect(() => {
    Animated.timing(fadeAnim, {
      toValue: 1,
      duration: 1200,
      useNativeDriver: true,
    }).start(() => {
        navigation.navigate('Home')
    });
  }, [fadeAnim]);

    return(
    <View style={styles.container}>
            <Animated.View
            style={{
            alignItems:'center',
            opacity: fadeAnim, 
            }}>
            <Image source={drivingApp}/>
            <Text style={styles.label}>
                Driving App
            </Text>
            </Animated.View>
  </View>
    )
}

const styles = StyleSheet.create({
    container: {flex: 1, alignItems: 'center', justifyContent: 'center',backgroundColor: '#EEEEEE'},
    sub_container: {alignItems:'center', justifyContent:'center'},
    label: {fontSize: 28, fontWeight: 600, color:'#000000'}
});

export default Splash