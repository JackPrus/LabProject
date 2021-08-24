package by.prus.LabProject.service.relgenerator;

import by.prus.LabProject.controller.CertificateTagController;
import by.prus.LabProject.controller.GiftCertificateController;
import by.prus.LabProject.controller.TagController;
import by.prus.LabProject.controller.UserController;
import by.prus.LabProject.model.dto.UserDto;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.model.response.TagResponse;
import by.prus.LabProject.model.response.UserResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
public class LinkCreator {

    public CollectionModel<GiftCertificateResponse> getNavigationCertLinks(Collection<GiftCertificateResponse> workValue, int page, int limit){

        CollectionModel<GiftCertificateResponse> returnValue = CollectionModel.of(workValue);

        //если последняя страница - то не будет ссылки на следующую страницу
        if (workValue.size()>=limit){
            Link nextPage = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                            .getCertificates(page+1, limit))
                    .withRel("nextPageRel");
            returnValue.add(nextPage);
        }
        //если первая страница не будет ссылки на предыдущую страницу
        if (page>1){
            Link previousPage = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                            .getCertificates(page-1, limit))
                    .withRel("previousPageRel");
            returnValue.add(previousPage);
        }

        return returnValue;
    }

    public CollectionModel<TagResponse> getNavigationTagLinks(Collection<TagResponse> workValue, int page, int limit){

        CollectionModel<TagResponse> returnValue = CollectionModel.of(workValue);

        //если последняя страница - то не будет ссылки на следующую страницу
        if (workValue.size()>=limit){
            Link nextPage = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(TagController.class)
                            .getTags(page+1, limit))
                    .withRel("nextPageRel");
            returnValue.add(nextPage);
        }
        //если первая страница не будет ссылки на предыдущую страницу
        if (page>1){
            Link previousPage = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(TagController.class)
                            .getTags(page-1, limit))
                    .withRel("previousPageRel");
            returnValue.add(previousPage);
        }
        return returnValue;
    }

    public CollectionModel<UserResponse> getNavigationUserLinks(Collection<UserResponse> workValue, int page, int limit){

        CollectionModel<UserResponse> returnValue = CollectionModel.of(workValue);

        //если последняя страница - то не будет ссылки на следующую страницу
        if (workValue.size()>=limit){
            Link nextPage = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserController.class)
                            .getUsers(page+1, limit))
                    .withRel("nextPageRel");
            returnValue.add(nextPage);
        }
        //если первая страница не будет ссылки на предыдущую страницу
        if (page>1){
            Link previousPage = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserController.class)
                            .getUsers(page-1, limit))
                    .withRel("previousPageRel");
            returnValue.add(previousPage);
        }

        return returnValue;
    }


    public CollectionModel<GiftCertificateResponse> getNavigationCertSearchByTagLinks(Collection<GiftCertificateResponse> workValue, String tagName, int page, int limit){

        CollectionModel<GiftCertificateResponse> returnValue = CollectionModel.of(workValue);

        //если последняя страница - то не будет ссылки на следующую страницу
        if (workValue.size()>=limit){
            Link nextPage = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(CertificateTagController.class)
                            .findCertificatesByTag(tagName, page+1, limit))
                    .withRel("nextPageRel");
            returnValue.add(nextPage);
        }
        //если первая страница не будет ссылки на предыдущую страницу
        if (page>1){
            Link previousPage = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(CertificateTagController.class)
                            .findCertificatesByTag(tagName,page-1, limit))
                    .withRel("previousPageRel");
            returnValue.add(previousPage);
        }

        return returnValue;
    }


    //http://localhost:8080/labproject/certificate/{iD}
    public void addLinkToCertificateResponse(GiftCertificateResponse certResponse){
        Link tagLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .getCertificate(certResponse.getId()))
                .withRel("certRel");
        certResponse.add(tagLink);
    }

    //http://localhost:8080/labproject/tag/{iD}
    public void addLinkToTagResponse(TagResponse tagResponse){
        Link tagLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TagController.class)
                        .getTag(tagResponse.getId()))
                .withRel("tagRel");
        tagResponse.add(tagLink);
    }

    //http://localhost:8080/labproject/user/{userId}
    public void addLinkToUserResponse (UserResponse userResponse){
        Link tagLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUser(userResponse.getUserId()))
                .withRel("userRel");
        userResponse.add(tagLink);
    }

}
