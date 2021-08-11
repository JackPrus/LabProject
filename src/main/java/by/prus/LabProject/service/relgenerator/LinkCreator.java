package by.prus.LabProject.service.relgenerator;

import by.prus.LabProject.controller.GiftCertificateController;
import by.prus.LabProject.controller.TagController;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.model.response.TagResponse;
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

    public void addLinkToCertificateResponse(GiftCertificateResponse certResponse){
        Link tagLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .getCertificate(certResponse.getId()))
                .withRel("certRel");
        certResponse.add(tagLink);
    }

    public void addLinkToTagResponse(TagResponse tagResponse){
        Link tagLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TagController.class)
                        .getTag(tagResponse.getId()))
                .withRel("tagRel");
        tagResponse.add(tagLink);
    }

}
