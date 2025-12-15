package com.hatterscraft.hatters_backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hatterscraft.hatters_backend.model.WikiPage;
import com.hatterscraft.hatters_backend.model.WikiPageStatus;
import com.hatterscraft.hatters_backend.repository.WikiPageRepository;
import com.hatterscraft.hatters_backend.service.WikiPageService;


@ExtendWith(MockitoExtension.class)
class WikiPageServiceTest {

    @Mock
    private WikiPageRepository repo;

    @InjectMocks
    private WikiPageService service;

    @Test
    void publish_shouldChangeStatus() {
        WikiPage page = new WikiPage();
        page.setStatus(WikiPageStatus.DRAFT);

        when(repo.findById(1L)).thenReturn(Optional.of(page));
        when(repo.save(page)).thenReturn(page);

        WikiPage published = service.publish(1L);

        assertThat(published.getStatus())
                .isEqualTo(WikiPageStatus.PUBLISHED);
    }

    @Test
    void getPublishedByCategory_shouldReturnOnlyPublished() {
        when(repo.findByCategoryAndStatus("guides", WikiPageStatus.PUBLISHED))
                .thenReturn(List.of(new WikiPage()));

        List<WikiPage> pages = service.getPublishedByCategory("guides");

        assertThat(pages).hasSize(1);
    }
}
