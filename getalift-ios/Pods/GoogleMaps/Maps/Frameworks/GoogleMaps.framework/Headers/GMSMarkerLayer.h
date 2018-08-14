//
//  GMSMarkerLayer.h
//  Google Maps SDK for iOS
//
//  Copyright 2013 Google Inc.
//
//  Usage of this SDK is subject to the Google Maps/Google Earth APIs Terms of
//  Service: https://developers.google.com/maps/terms
//

#import <CoreLocation/CoreLocation.h>
#import <QuartzCore/QuartzCore.h>

<<<<<<< HEAD
NS_ASSUME_NONNULL_BEGIN;

/**
 * GMSMarkerLayer is a custom subclass of CALayer, available on a per-marker basis, that allows
=======
#import "GMSOverlayLayer.h"

NS_ASSUME_NONNULL_BEGIN;

/**
 * GMSMarkerLayer is a subclass of GMSOverlayLayer, available on a per-marker basis, that allows
>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d
 * animation of several properties of its associated GMSMarker.
 *
 * Note that this CALayer is never actually rendered directly, as GMSMapView is provided entirely
 * via an OpenGL layer. As such, adjustments or animations to 'default' properties of CALayer will
 * not have any effect.
 */
<<<<<<< HEAD
@interface GMSMarkerLayer : CALayer
=======
@interface GMSMarkerLayer : GMSOverlayLayer
>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d

/** Latitude, part of |position| on GMSMarker. */
@property(nonatomic, assign) CLLocationDegrees latitude;

/** Longitude, part of |position| on GMSMarker. */
@property(nonatomic, assign) CLLocationDegrees longitude;

/** Rotation, as per GMSMarker. */
@property(nonatomic, assign) CLLocationDegrees rotation;

/** Opacity, as per GMSMarker. */
@property(atomic, assign) float opacity;

@end

extern NSString *const kGMSMarkerLayerLatitude;
extern NSString *const kGMSMarkerLayerLongitude;
extern NSString *const kGMSMarkerLayerRotation;
extern NSString *const kGMSMarkerLayerOpacity;

NS_ASSUME_NONNULL_END;
