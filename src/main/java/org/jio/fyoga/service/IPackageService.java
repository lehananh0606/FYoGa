package org.jio.fyoga.service;/*  Welcome to Jio word
    @author: Jio
    Date: 6/25/2023
    Time: 12:21 AM
    
    ProjectName: demoSpring02
    Jio: I wish you always happy with coding <3
*/

import org.jio.fyoga.entity.Package;

import java.util.List;
import java.util.Optional;

public interface IPackageService {

    List<Package> findAllByCourse_CourseID( int CourseID);

    Optional<Package> findById(Integer integer);
}
